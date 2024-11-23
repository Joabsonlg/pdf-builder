---
layout: default
title: Paragraph
parent: Text Components
grand_parent: Components
nav_order: 1
---

# Paragraph
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

The Paragraph component is the fundamental building block for text content in PDF Builder. It provides rich formatting options and supports mixed text styles within a single paragraph.

## Basic Usage

```java
Paragraph paragraph = Paragraph.builder()
    .addText("This is a simple paragraph.")
    .build();
```

## Features

### Mixed Text Styles

Combine different text styles within a single paragraph:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Regular text, ")
    .addBoldText("bold text, ")
    .addItalicText("italic text, ")
    .addBoldItalicText("bold italic text")
    .build();
```

### Text Alignment

Control how text is aligned within the paragraph:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Aligned text")
    .withAlignment(TextAlignment.CENTER)  // CENTER, LEFT, RIGHT, or JUSTIFIED
    .build();
```

### Font Settings

Customize font properties:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Custom font text")
    .withFont(Font.builder()
        .withName("Times New Roman")
        .withSize(14f)
        .withStyle(FontStyle.NORMAL)
        .build())
    .build();
```

### Line Spacing

Adjust the space between lines:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Text with custom line spacing")
    .withLineSpacing(1.5f)  // 1.5 times normal spacing
    .build();
```

### Margins

Set paragraph margins:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Text with margins")
    .withMargins(Margin.builder()
        .withTop(10f)
        .withRight(20f)
        .withBottom(10f)
        .withLeft(20f)
        .build())
    .build();
```

### Text Color

Apply color to text:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Colored text")
    .withColor(Color.BLUE)  // Predefined color
    .build();

// Or using RGB values
paragraph = Paragraph.builder()
    .addText("Custom colored text")
    .withColor(Color.rgb(100, 150, 200))
    .build();
```

### Text Decoration

Add underline or strikethrough:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Regular text ")
    .addUnderlinedText("underlined text ")
    .addStrikethroughText("strikethrough text")
    .build();
```

### Hyphenation

Control word hyphenation:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Long text that might need hyphenation")
    .withHyphenation(true)
    .withHyphenationLanguage("en-US")
    .build();
```

## Advanced Features

### Text Indentation

Control first line and hanging indentation:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Indented paragraph text")
    .withFirstLineIndent(20f)
    .withHangingIndent(10f)
    .build();
```

### Background Color

Add background color to paragraphs:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Text with background")
    .withBackgroundColor(Color.LIGHT_GRAY)
    .build();
```

### Border

Add borders around paragraphs:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Bordered paragraph")
    .withBorder(Border.builder()
        .withColor(Color.BLACK)
        .withWidth(1f)
        .withStyle(BorderStyle.SOLID)
        .build())
    .build();
```

### Spacing

Control spacing before and after paragraphs:

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Spaced paragraph")
    .withSpacingBefore(10f)
    .withSpacingAfter(10f)
    .build();
```

## Complex Examples

### Rich Text Formatting

```java
Paragraph paragraph = Paragraph.builder()
    .addText("This is ")
    .addBoldText("important ")
    .addText("and ")
    .addItalicText("emphasized ")
    .addText("text with ")
    .addUnderlinedText("underlying ")
    .addText("meaning.")
    .withFont(Font.builder()
        .withName("Arial")
        .withSize(12f)
        .build())
    .withAlignment(TextAlignment.JUSTIFIED)
    .withLineSpacing(1.5f)
    .withMargins(Margin.builder()
        .withAll(10f)
        .build())
    .build();
```

### Academic Style

```java
Paragraph paragraph = Paragraph.builder()
    .addText("According to Smith (2023), ")
    .addItalicText("\"the study showed significant results\" ")
    .addText("(p. 42).")
    .withFont(Font.builder()
        .withName("Times New Roman")
        .withSize(12f)
        .build())
    .withAlignment(TextAlignment.JUSTIFIED)
    .withLineSpacing(2.0f)  // Double spacing
    .withFirstLineIndent(24f)  // Half inch indent
    .build();
```

### Marketing Content

```java
Paragraph paragraph = Paragraph.builder()
    .addBoldText("Special Offer: ")
    .addText("Get ")
    .addBoldText("50% OFF ")
    .addText("on all products!")
    .withFont(Font.builder()
        .withName("Helvetica")
        .withSize(14f)
        .build())
    .withAlignment(TextAlignment.CENTER)
    .withColor(Color.rgb(0, 102, 204))
    .withBackgroundColor(Color.rgb(240, 240, 240))
    .withPadding(Padding.builder()
        .withAll(10f)
        .build())
    .withBorder(Border.builder()
        .withColor(Color.rgb(0, 102, 204))
        .withWidth(1f)
        .withStyle(BorderStyle.DASHED)
        .build())
    .build();
```

## Best Practices

1. **Text Styling**
   - Use consistent fonts throughout the document
   - Limit the number of different styles in one paragraph
   - Use bold and italic sparingly for emphasis

2. **Spacing**
   - Use consistent line spacing
   - Add appropriate margins for readability
   - Consider the document's overall spacing rhythm

3. **Alignment**
   - Use justified text for formal documents
   - Center alignment for titles and headings
   - Left alignment for general content

4. **Performance**
   - Reuse font and style objects when possible
   - Avoid excessive text decorations
   - Consider memory usage with large documents

## Common Issues

### Line Breaking

Problem: Unexpected line breaks in text.

Solution:
```java
Paragraph paragraph = Paragraph.builder()
    .addText("Long text content")
    .withWordWrap(true)
    .withHyphenation(true)
    .build();
```

### Font Embedding

Problem: Custom fonts not appearing correctly.

Solution:
```java
Font font = Font.builder()
    .withName("CustomFont")
    .withPath("fonts/custom.ttf")
    .withEmbedded(true)
    .build();

Paragraph paragraph = Paragraph.builder()
    .addText("Text with custom font")
    .withFont(font)
    .build();
```

### Text Overflow

Problem: Text extending beyond margins.

Solution:
```java
Paragraph paragraph = Paragraph.builder()
    .addText("Long text content")
    .withWordWrap(true)
    .withOverflow(Overflow.SPLIT)
    .build();
```

## Next Steps

- Learn about [Heading](heading) components
- Explore [List](list) components
- See [Text](text) component for inline styling
- Check out [Layout Components](../layout) for structural elements
