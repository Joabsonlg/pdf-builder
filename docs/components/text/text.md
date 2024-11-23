---
layout: default
title: Text
parent: Text Components
grand_parent: Components
nav_order: 4
---

# Text
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

The Text component is used for inline text styling and is typically used within other components like Paragraph, ListItem, or TableCell. It provides fine-grained control over text appearance and formatting.

## Basic Usage

```java
Text text = Text.builder()
    .withContent("Simple text")
    .build();
```

## Features

### Text Styling

Apply various text styles:

```java
Text text = Text.builder()
    .withContent("Styled text")
    .withFont(Font.builder()
        .withName("Arial")
        .withSize(12f)
        .withStyle(FontStyle.BOLD)
        .build())
    .withColor(Color.BLUE)
    .build();
```

### Font Management

Control font properties:

```java
Text text = Text.builder()
    .withContent("Custom font text")
    .withFont(Font.builder()
        .withName("Times New Roman")
        .withSize(14f)
        .withStyle(FontStyle.ITALIC)
        .build())
    .build();
```

### Text Decoration

Add underline or strikethrough:

```java
Text text = Text.builder()
    .withContent("Decorated text")
    .withUnderline(true)
    .withUnderlineStyle(UnderlineStyle.SOLID)
    .withUnderlineColor(Color.RED)
    .build();

Text strikethrough = Text.builder()
    .withContent("Strikethrough text")
    .withStrikethrough(true)
    .withStrikethroughColor(Color.GRAY)
    .build();
```

### Character Spacing

Adjust space between characters:

```java
Text text = Text.builder()
    .withContent("Spaced text")
    .withCharacterSpacing(0.5f)
    .build();
```

### Background Color

Add background highlighting:

```java
Text text = Text.builder()
    .withContent("Highlighted text")
    .withBackgroundColor(Color.YELLOW)
    .build();
```

## Advanced Features

### Superscript and Subscript

Create superscript and subscript text:

```java
Text superscript = Text.builder()
    .withContent("2")
    .withSuperscript(true)
    .build();

Text subscript = Text.builder()
    .withContent("2")
    .withSubscript(true)
    .build();
```

### Text Rise

Adjust text baseline position:

```java
Text raised = Text.builder()
    .withContent("Raised text")
    .withRise(5f)
    .build();
```

### Text Rendering Mode

Control how text is rendered:

```java
Text outlined = Text.builder()
    .withContent("Outlined text")
    .withRenderingMode(RenderingMode.STROKE)
    .withStrokeColor(Color.BLACK)
    .withStrokeWidth(0.5f)
    .build();
```

### Custom Fonts

Use custom fonts:

```java
Text customFont = Text.builder()
    .withContent("Custom font text")
    .withFont(Font.builder()
        .withName("CustomFont")
        .withPath("fonts/custom.ttf")
        .withEmbedded(true)
        .build())
    .build();
```

## Complex Examples

### Mathematical Expression

```java
Paragraph equation = Paragraph.builder()
    .add(Text.builder()
        .withContent("E = mc")
        .build())
    .add(Text.builder()
        .withContent("2")
        .withSuperscript(true)
        .withSize(8f)
        .build())
    .build();
```

### Rich Text Formatting

```java
Paragraph richText = Paragraph.builder()
    .add(Text.builder()
        .withContent("Important: ")
        .withFont(Font.builder()
            .withName("Arial")
            .withSize(12f)
            .withStyle(FontStyle.BOLD)
            .build())
        .withColor(Color.RED)
        .build())
    .add(Text.builder()
        .withContent("This text contains ")
        .build())
    .add(Text.builder()
        .withContent("highlighted")
        .withBackgroundColor(Color.YELLOW)
        .build())
    .add(Text.builder()
        .withContent(" and ")
        .build())
    .add(Text.builder()
        .withContent("underlined")
        .withUnderline(true)
        .withUnderlineColor(Color.BLUE)
        .build())
    .add(Text.builder()
        .withContent(" portions.")
        .build())
    .build();
```

### Technical Documentation

```java
Paragraph technical = Paragraph.builder()
    .add(Text.builder()
        .withContent("The ")
        .build())
    .add(Text.builder()
        .withContent("velocity (v)")
        .withFont(Font.builder()
            .withName("Times New Roman")
            .withStyle(FontStyle.ITALIC)
            .build())
        .build())
    .add(Text.builder()
        .withContent(" is measured in m/s")
        .build())
    .add(Text.builder()
        .withContent("2")
        .withSuperscript(true)
        .withSize(8f)
        .build())
    .build();
```

## Best Practices

1. **Font Usage**
   - Use consistent fonts
   - Embed custom fonts
   - Consider fallback fonts

2. **Styling**
   - Use styles purposefully
   - Maintain readability
   - Consider contrast

3. **Performance**
   - Reuse font objects
   - Limit custom fonts
   - Optimize text content

4. **Accessibility**
   - Use appropriate font sizes
   - Maintain good contrast
   - Consider reading order

## Common Issues

### Font Embedding

Problem: Custom fonts not displaying correctly.

Solution:
```java
Text text = Text.builder()
    .withContent("Custom font text")
    .withFont(Font.builder()
        .withName("CustomFont")
        .withPath("fonts/custom.ttf")
        .withEmbedded(true)
        .withFallback("Arial")
        .build())
    .build();
```

### Character Encoding

Problem: Special characters not displaying correctly.

Solution:
```java
Text text = Text.builder()
    .withContent("Special characters: ©®™")
    .withFont(Font.builder()
        .withName("Arial Unicode MS")
        .build())
    .withEncoding(Encoding.UTF_8)
    .build();
```

### Text Positioning

Problem: Text not aligning properly with other elements.

Solution:
```java
Text text = Text.builder()
    .withContent("Aligned text")
    .withBaseline(BaselineAlignment.MIDDLE)
    .withRise(0f)
    .build();
```

## Next Steps

- Learn about [Paragraph](paragraph) components
- Explore [Heading](heading) components
- See [List](list) components
- Check out [Layout Components](../layout) for structural elements
