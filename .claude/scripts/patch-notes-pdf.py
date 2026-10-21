"""Turn a patch-notes Markdown file into a PDF styled like the game.

Usage: python .claude/scripts/patch-notes-pdf.py [notes.md] [out.pdf]

With no arguments it takes the newest file in docs/patch-notes/ and writes the PDF next to it.
The page uses the game's own assets from src/main/resources/images: a nebula background, the title
logo, the pixel letters for headings, and each item's shop icon and rarity colour. Item icons and
rarities are read from ItemEnums.java and ImageLoader.java, so new items pick them up by themselves.

It needs only the Python standard library plus Google Chrome or Microsoft Edge, which renders the
PDF in headless mode. Set the CHROME environment variable to point at another browser binary.
"""
import html
import os
import re
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
NOTES_DIR = REPO / "docs" / "patch-notes"
RESOURCES = REPO / "src" / "main" / "resources"
IMAGES = RESOURCES / "images"
JAVA = REPO / "src" / "main" / "java" / "net" / "riezebos" / "bruus" / "tbd"
ITEM_ENUMS = JAVA / "game" / "items" / "ItemEnums.java"
IMAGE_LOADER = JAVA / "visualsandaudio" / "data" / "image" / "ImageLoader.java"

BACKGROUND = IMAGES / "background" / "Blue Nebula 1 - 1024x1024.png"
TITLE_IMAGE = IMAGES / "TitleImage.png"
ICON_FRAME = IMAGES / "UI" / "IconFrame.png"

# The colours from ItemRarityEnums.java (java.awt.Color constants).
RARITY_COLOURS = {"Common": "#ffffff", "Rare": "#00ff00", "Legendary": "#ffc800", "Relic": "#ffff00"}

# Pixel letter sprites in images/Letters for the characters that have no "Letter-<char>.png" name.
LETTER_FILES = {"H": "Letter-H van Hooi.png", ".": "Letter-Dot.png", ":": "Letter-DubbelePunt.png",
                ";": "Letter-PuntKomma.png", ",": "komma.png", "%": "percent.png",
                "=": "Letter-Equals.png", "<": "Letter-SmallerThan.png", ">": "Letter-GreaterThan.png"}

BROWSER_CANDIDATES = [
    r"C:\Program Files\Google\Chrome\Application\chrome.exe",
    r"C:\Program Files (x86)\Google\Chrome\Application\chrome.exe",
    r"C:\Program Files\Microsoft\Edge\Application\msedge.exe",
    r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe",
    "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
    "google-chrome", "chromium", "chromium-browser", "chrome", "msedge",
]

CSS = """
@page { size: A4; margin: 0; }
* { -webkit-print-color-adjust: exact; print-color-adjust: exact; box-sizing: border-box; }
html {
  background: linear-gradient(rgba(4, 6, 20, .55), rgba(4, 6, 20, .55)), url("{background}") #05060f;
  background-size: auto, 210mm 210mm;
}
body { margin: 0; padding: 0 14mm; font-family: Helvetica, Arial, sans-serif; font-size: 10pt;
       color: #e6f8ff; line-height: 1.4; }
table.page { width: 100%; border-collapse: collapse; }
table.page td { padding: 0; }
.spacer { height: 12mm; }
.title { text-align: center; padding-top: 4mm; }
.title img.logo { width: 120mm; }
.subtitle { margin-top: 3mm; }
.date { margin-top: 2mm; }
.pixel { display: inline-flex; align-items: flex-end; gap: 1px; }
.pixel img { image-rendering: pixelated; height: var(--h); }
.pixel .gap { width: calc(var(--h) * .55); }
.pixwrap { position: relative; display: inline-block; }
.pixwrap .copy { position: absolute; left: 0; top: 0; z-index: -1; color: rgba(255, 255, 255, .02); font-size: 6pt; white-space: nowrap; }
.pixel .plain { font-weight: 700; font-size: var(--h); line-height: 1; }
.glow { filter: drop-shadow(0 0 3px rgba(127, 220, 255, .9)) drop-shadow(0 0 7px rgba(127, 220, 255, .45)); }
section { margin-top: 8mm; }
section h2 { margin: 0 0 3mm; break-after: avoid; }
.panel { background: rgba(2, 18, 22, .78); border: 1.5px solid rgb(127, 220, 255); border-radius: 10px;
         box-shadow: 0 0 10px rgba(127, 220, 255, .55), inset 0 0 14px rgba(127, 220, 255, .12);
         padding: 3mm 5mm; }
ul { margin: 1px 0; padding-left: 16px; }
li { margin: 1px 0; }
li::marker { color: rgb(127, 220, 255); }
.panel > ul { list-style: none; padding-left: 0; }
.panel > ul > li { break-inside: avoid; padding: 2mm 0; border-bottom: 1px solid rgba(127, 220, 255, .18); }
.panel > ul > li:last-child { border-bottom: 0; }
.entry { display: flex; gap: 3mm; align-items: flex-start; }
.icon { flex: none; width: 11mm; height: 11mm; background: url("{icon_frame}") center / 100% 100%;
        image-rendering: pixelated; padding: 1.2mm; }
.icon.blank { background: none; }
.icon img { width: 100%; height: 100%; object-fit: contain; display: block; }
.entry .body { flex: 1; min-width: 0; }
.name { font-weight: 700; font-size: 11pt; }
.panel > ul > li > .entry .body > ul { padding-left: 14px; }
.new { color: #001014; background: rgb(127, 220, 255); font-size: 7pt; font-weight: 800; padding: 0 4px;
       border-radius: 3px; letter-spacing: .5px; vertical-align: 1px; box-shadow: 0 0 5px rgba(127, 220, 255, .8); }
strong { color: #ffc800; }
p { margin: 3px 0; }
"""


def find_browser():
    for candidate in [os.environ.get("CHROME")] + BROWSER_CANDIDATES:
        if not candidate:
            continue
        if Path(candidate).is_file():
            return candidate
        found = shutil.which(candidate)
        if found:
            return found
    sys.exit("No Chrome or Edge found. Set the CHROME environment variable to its path.")


def load_items():
    """Map each item's display name (lower case) to its rarity and icon file, read from the Java code."""
    image_paths = {}
    pending = []
    for line in IMAGE_LOADER.read_text(encoding="utf-8").splitlines():
        pending += re.findall(r"case\s+(\w+)\s*:", line)
        found = re.search(r'return\s+"(/images/[^"]+)"', line)
        if found:
            for name in pending:
                image_paths.setdefault(name, found.group(1))
            pending = []
    items = {}
    pattern = r'^\s*\w+\(ItemRarityEnums\.(\w+),\s*ImageEnums\.(\w+),\s*"([^"]+)"'
    for rarity, image, name in re.findall(pattern, ITEM_ENUMS.read_text(encoding="utf-8"), re.M):
        path = image_paths.get(image)
        icon = RESOURCES / path.lstrip("/") if path else None
        items[name.lower()] = (rarity, icon if icon and icon.is_file() else None)
    return items


def match_item(text, items):
    """Find the item whose display name starts the entry text, preferring the longest name."""
    lowered = text.lower()
    best = None
    for name in items:
        if lowered.startswith(name) and (len(lowered) == len(name) or lowered[len(name)] in " (:"):
            if best is None or len(name) > len(best):
                best = name
    return items[best] if best else None


def pixel_text(text, height, glow=True):
    parts = []
    for char in text:
        if char == " ":
            parts.append('<span class="gap"></span>')
            continue
        if char in LETTER_FILES:
            file = IMAGES / "Letters" / LETTER_FILES[char]
        elif char.islower():
            file = IMAGES / "Letters" / f"Letter-Lowercase{char.upper()}.png"
        else:
            file = IMAGES / "Letters" / f"Letter-{char}.png"
        if file.is_file():
            parts.append(f'<img src="{file.as_uri()}" alt="{html.escape(char)}">')
        else:
            parts.append(f'<span class="plain">{html.escape(char)}</span>')
    cls = "pixel glow" if glow else "pixel"
    # Invisible real text keeps the heading searchable and copyable in the PDF. It sits outside the
    # glow, because Chrome turns everything under a CSS filter into a picture.
    return (f'<span class="pixwrap"><span class="copy">{html.escape(text)}</span>'
            f'<span class="{cls}" style="--h:{height}">{"".join(parts)}</span></span>')


def inline(text):
    text = html.escape(text.strip())
    text = re.sub(r"\*\*(.+?)\*\*", r"<strong>\1</strong>", text)
    return re.sub(r"\s*\(NEW\)", ' <span class="new">NEW</span>', text)


def render_list(items):
    if not items:
        return ""
    return "<ul>" + "".join(f"<li>{inline(i['text'])}{render_list(i['children'])}</li>"
                            for i in items) + "</ul>"


def render_entries(entries, items):
    matches = [match_item(entry["text"], items) for entry in entries]
    # When any entry in a section has an icon, entries without one keep an empty slot, so all
    # icons sit in one column and all names start at the same place.
    any_icon = any(found and found[1] for found in matches)
    parts = []
    for entry, found in zip(entries, matches):
        colour = RARITY_COLOURS.get(found[0], "#e6f8ff") if found else "#e6f8ff"
        icon = '<div class="icon blank"></div>' if any_icon else ""
        if found and found[1]:
            icon = f'<div class="icon"><img src="{found[1].as_uri()}"></div>'
        name = inline(entry["text"].rstrip().rstrip(":"))
        parts.append(f'<li><div class="entry">{icon}<div class="body">'
                     f'<div class="name" style="color:{colour}">{name}</div>'
                     f'{render_list(entry["children"])}</div></div></li>')
    return "<ul>" + "".join(parts) + "</ul>"


def to_html(markdown, date):
    items = load_items()
    lines = markdown.splitlines()
    sections = []  # each: {"title": str or None, "entries": [...], "paragraphs": [...]}
    current = {"title": None, "entries": [], "paragraphs": []}
    stack = [(-1, {"children": current["entries"]})]

    for line in lines[1:]:
        stripped = line.strip()
        # Skip empty lines and unfilled template placeholders such as "<intent: ...>".
        if not stripped or re.fullmatch(r"<[^>]*>", stripped):
            continue
        item = re.match(r"^(\s*)-\s+(.*)$", line)
        if item:
            indent = len(item.group(1).replace("\t", "    "))
            while stack[-1][0] >= indent:
                stack.pop()
            node = {"text": item.group(2), "children": []}
            stack[-1][1]["children"].append(node)
            stack.append((indent, node))
        elif not line[0].isspace() and stripped.endswith(":"):
            sections.append(current)
            current = {"title": stripped[:-1], "entries": [], "paragraphs": []}
            stack = [(-1, {"children": current["entries"]})]
        else:
            current["paragraphs"].append(stripped)
    sections.append(current)

    body = []
    for section in sections:
        if not section["entries"] and not section["paragraphs"]:
            continue
        heading = f"<h2>{pixel_text(section['title'].upper(), '7mm')}</h2>" if section["title"] else ""
        paragraphs = "".join(f"<p>{inline(p)}</p>" for p in section["paragraphs"])
        body.append(f'<section>{heading}<div class="panel">{paragraphs}'
                    f'{render_entries(section["entries"], items)}</div></section>')

    title = lines[0].strip() if lines else "Patch notes"
    css = CSS.replace("{background}", BACKGROUND.as_uri()).replace("{icon_frame}", ICON_FRAME.as_uri())
    header = (f'<div class="title"><img class="logo" src="{TITLE_IMAGE.as_uri()}">'
              f'<div class="subtitle">{pixel_text(title.upper(), "9mm")}</div>'
              f'<div class="date">{pixel_text(date.replace("-", "."), "5mm", glow=False)}</div></div>')
    return (f"<!doctype html><html><head><meta charset='utf-8'><title>{html.escape(title)}</title>"
            f"<style>{css}</style></head><body><table class='page'>"
            f"<thead><tr><td><div class='spacer'></div></td></tr></thead>"
            f"<tfoot><tr><td><div class='spacer'></div></td></tr></tfoot>"
            f"<tbody><tr><td>{header}{''.join(body)}</td></tr></tbody></table></body></html>")


def main():
    if len(sys.argv) > 1:
        source = Path(sys.argv[1]).resolve()
    else:
        notes = sorted(NOTES_DIR.glob("*.md"))
        if not notes:
            sys.exit(f"No patch notes found in {NOTES_DIR}")
        source = notes[-1]
    target = Path(sys.argv[2]).resolve() if len(sys.argv) > 2 else source.with_suffix(".pdf")

    page = to_html(source.read_text(encoding="utf-8"), source.stem)
    with tempfile.TemporaryDirectory() as tmp:
        html_file = Path(tmp) / "notes.html"
        html_file.write_text(page, encoding="utf-8")
        subprocess.run([find_browser(), "--headless=new", "--disable-gpu", "--no-pdf-header-footer",
                        "--allow-file-access-from-files", f"--user-data-dir={Path(tmp) / 'profile'}",
                        f"--print-to-pdf={target}", html_file.as_uri()],
                       check=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    if not target.is_file():
        sys.exit("The browser did not write the PDF.")
    print(target)


if __name__ == "__main__":
    main()
