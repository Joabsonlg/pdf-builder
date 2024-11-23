---
layout: default
title: Heading
parent: Text Components
grand_parent: Components
nav_order: 2
---

# Heading
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

The Heading component is used to create section headers in your PDF documents. It supports multiple heading levels and provides rich styling options to create a clear document hierarchy.

## Basic Usage

```java
Heading heading = Heading.builder()
    .withText("Chapter 1: Introduction")
    .withLevel(1)
    .build();
```

## Features

### Heading Levels

PDF Builder supports six heading levels (1-6):

```java
// Level 1 - Main heading
Heading h1 = Heading.builder()
    .withText("Document Title")
    .withLevel(1)
    .build();

// Level 2 - Section heading
Heading h2 = Heading.builder()
    .withText("Section Title")
    .withLevel(2)
    .build();

// Level 3 - Subsection heading
Heading h3 = Heading.builder()
    .withText("Subsection Title")
    .withLevel(3)
    .build();
```

### Default Styles

Each heading level comes with default styling:

| Level | Font Size | Font Weight | Spacing |
|-------|-----------|-------------|----------|
| H1 | 24pt | Bold | 24pt before, 16pt after |
| H2 | 20pt | Bold | 20pt before, 14pt after |
| H3 | 16pt | Bold | 16pt before, 12pt after |
| H4 | 14pt | Bold | 14pt before, 10pt after |
| H5 | 12pt | Bold | 12pt before, 8pt after |
| H6 | 10pt | Bold | 10pt before, 6pt after |

### Custom Styling

Override default styles:

```java
Heading heading = Heading.builder()
    .withText("Custom Styled Heading")
    .withLevel(1)
    .withFont(Font.builder()
        .withName("Georgia")
        .withSize(28f)
        .withStyle(FontStyle.BOLD)
        .build())
    .withColor(Color.rgb(0, 51, 102))
    .withAlignment(TextAlignment.CENTER)
    .build();
```

### Spacing Control

Adjust spacing around headings:

```java
Heading heading = Heading.builder()
    .withText("Spaced Heading")
    .withLevel(1)
    .withSpacingBefore(30f)
    .withSpacingAfter(20f)
    .build();
```

### Automatic Numbering

Enable automatic heading numbering:

```java
Heading heading = Heading.builder()
    .withText("Numbered Heading")
    .withLevel(1)
    .withNumbering(true)
    .withNumberingFormat("%d.") // "1.", "2.", etc.
    .build();
```

### Custom Numbering Format

Define custom numbering formats:

```java
Heading heading = Heading.builder()
    .withText("Custom Numbered Heading")
    .withLevel(2)
    .withNumbering(true)
    .withNumberingFormat("Chapter %d:") // "Chapter 1:", etc.
    .withNumberingStyle(NumberingStyle.ROMAN) // I, II, III, etc.
    .build();
```

## Advanced Features

### Page Breaking

Control page breaks around headings:

```java
Heading heading = Heading.builder()
    .withText("New Page Heading")
    .withLevel(1)
    .withPageBreakBefore(true)
    .withKeepWithNext(true) // Prevents orphan headings
    .build();
```

### Bookmarks

Automatically create PDF bookmarks:

```java
Heading heading = Heading.builder()
    .withText("Bookmarked Heading")
    .withLevel(1)
    .withBookmark(true)
    .build();
```

### Background and Borders

Add visual emphasis:

```java
Heading heading = Heading.builder()
    .withText("Styled Heading")
    .withLevel(1)
    .withBackgroundColor(Color.LIGHT_GRAY)
    .withBorder(Border.builder()
        .withColor(Color.DARK_GRAY)
        .withWidth(1f)
        .withStyle(BorderStyle.SOLID)
        .build())
    .withPadding(Padding.builder()
        .withAll(10f)
        .build())
    .build();
```

## Complex Examples

### Chapter Heading

```java
Heading chapterHeading = Heading.builder()
    .withText("Chapter 1: Getting Started")
    .withLevel(1)
    .withFont(Font.builder()
        .withName("Georgia")
        .withSize(28f)
        .withStyle(FontStyle.BOLD)
        .build())
    .withColor(Color.rgb(0, 51, 102))
    .withAlignment(TextAlignment.CENTER)
    .withSpacingBefore(50f)
    .withSpacingAfter(30f)
    .withPageBreakBefore(true)
    .withBookmark(true)
    .withNumbering(true)
    .withNumberingFormat("Chapter %d: ")
    .build();
```

### Decorative Section Heading

```java
Heading sectionHeading = Heading.builder()
    .withText("Product Features")
    .withLevel(2)
    .withFont(Font.builder()
        .withName("Helvetica")
        .withSize(20f)
        .withStyle(FontStyle.BOLD)
        .build())
    .withColor(Color.WHITE)
    .withBackgroundColor(Color.rgb(0, 102, 204))
    .withBorder(Border.builder()
        .withColor(Color.rgb(0, 51, 102))
        .withWidth(2f)
        .withStyle(BorderStyle.SOLID)
        .build())
    .withPadding(Padding.builder()
        .withVertical(10f)
        .withHorizontal(20f)
        .build())
    .withAlignment(TextAlignment.LEFT)
    .withBookmark(true)
    .build();
```

### Academic Style Heading

```java
Heading academicHeading = Heading.builder()
    .withText("Methodology")
    .withLevel(2)
    .withFont(Font.builder()
        .withName("Times New Roman")
        .withSize(14f)
        .withStyle(FontStyle.BOLD)
        .build())
    .withAlignment(TextAlignment.LEFT)
    .withSpacingBefore(24f)
    .withSpacingAfter(12f)
    .withNumbering(true)
    .withNumberingFormat("%d.%d ") // Creates "2.1 ", "2.2 ", etc.
    .withKeepWithNext(true)
    .build();
```

## Best Practices

1. **Hierarchy**
   - Use heading levels consistently
   - Don't skip levels (e.g., H1 to H3)
   - Keep hierarchy logical and clear

2. **Styling**
   - Maintain consistent styling per level
   - Ensure sufficient contrast
   - Use appropriate font sizes

3. **Spacing**
   - Add adequate spacing around headings
   - Consider page breaks carefully
   - Keep headings with following content

4. **Numbering**
   - Use consistent numbering formats
   - Consider document type and audience
   - Align with document standards

## Common Issues

### Orphaned Headings

Problem: Heading appears at bottom of page.

Solution:
```java
Heading heading = Heading.builder()
    .withText("Non-orphaned Heading")
    .withLevel(1)
    .withKeepWithNext(true)
    .build();
```

### Inconsistent Spacing

Problem: Irregular spacing around headings.

Solution:
```java
// Create reusable heading styles
HeadingStyle h1Style = HeadingStyle.builder()
    .withSpacingBefore(30f)
    .withSpacingAfter(20f)
    .build();

Heading heading = Heading.builder()
    .withText("Consistent Heading")
    .withLevel(1)
    .withStyle(h1Style)
    .build();
```

### Long Headings

Problem: Headings too long for page width.

Solution:
```java
Heading heading = Heading.builder()
    .withText("Very Long Heading That Might Need to Wrap")
    .withLevel(1)
    .withWordWrap(true)
    .withAlignment(TextAlignment.LEFT)
    .build();
```

## Next Steps

- Learn about [Paragraph](paragraph) components
- Explore [List](list) components
- See [Text](text) component for inline styling
- Check out [Layout Components](../layout) for structural elements
