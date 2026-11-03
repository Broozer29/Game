# A changelog conflict and its resolution

Two branches each added lines at the top of `CHANGELOG.md`. Nelis added a new date section; a feature branch added lines under an older date and a new date of its own.

## What git leaves in the file

```
<<<<<<< HEAD
## 2026-10-18

### Added
- Internal: the autoplay bot is steered by JSON scenario files.

## 2026-10-17

### Changed
- Internal: planboard follow-ups from the boss preloading.
=======
## 2026-10-17

### Changed
- Rotated pictures are now stored at their visible size.
- Internal: the image cache log shows how many MB are rotated copies.

## 2026-10-16

### Changed
- The image cache now also counts the copy the graphics card keeps.
>>>>>>> origin/Nelis
```

## The resolution

The 2026-10-18 section only one side has stays first. The 2026-10-17 section both sides have becomes one section, the branch's own lines first, then the lines merged in. The 2026-10-16 section only one side has follows.

```
## 2026-10-18

### Added
- Internal: the autoplay bot is steered by JSON scenario files.

## 2026-10-17

### Changed
- Internal: planboard follow-ups from the boss preloading.
- Rotated pictures are now stored at their visible size.
- Internal: the image cache log shows how many MB are rotated copies.

## 2026-10-16

### Changed
- The image cache now also counts the copy the graphics card keeps.
```

Every line of both sides is present once. `git diff HEAD` and `git diff MERGE_HEAD` on the file then show only added lines.
