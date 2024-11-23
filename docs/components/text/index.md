---
layout: default
title: Text Components
parent: Components
has_children: true
nav_order: 1
---

# Text Components
{: .no_toc }

Text components are the fundamental building blocks for adding textual content to your PDF documents. PDF Builder provides a rich set of text components that can be used to create well-structured and beautifully formatted documents.

## Available Components

### [Paragraph](paragraph)
The most basic text component for creating blocks of text with rich formatting options.

```java
Paragraph paragraph = Paragraph.builder()
    .addText("Regular text ")
    .addBoldText("bold text ")
    .addItalicText("italic text")
    .withAlignment(TextAlignment.JUSTIFIED)
    .withLineSpacing(1.5f)
    .build();
```

### [Heading](heading)
Create section headers with different levels and styling.

```java
Heading heading = Heading.builder()
    .withText("Chapter 1: Introduction")
    .withLevel(1)
    .withColor(Color.BLUE)
    .withFontSize(24f)
    .build();
```

### [List](list)
Create ordered and unordered lists with customizable markers.

```java
List list = List.builder()
    .withType(ListType.ORDERED)
    .addItem("First item")
    .addItem("Second item")
    .addItem("Third item")
    .withMarkerStyle(MarkerStyle.DECIMAL)
    .build();
```

### [Text](text)
Inline text with styling for use within other components.

```java
Text text = Text.builder()
    .withContent("Styled text")
    .withFont("Arial")
    .withSize(12f)
    .withColor(Color.RED)
    .withStyle(FontStyle.BOLD)
    .build();
```

## Common Features

All text components share these common features:

### Font Management

```java
component.withFont(Font.builder()
    .withName("Times New Roman")
    .withSize(12f)
    .withStyle(FontStyle.NORMAL)
    .build());
```

### Text Alignment

```java
component.withAlignment(TextAlignment.LEFT);     // Left align
component.withAlignment(TextAlignment.CENTER);   // Center align
component.withAlignment(TextAlignment.RIGHT);    // Right align
component.withAlignment(TextAlignment.JUSTIFIED); // Justified
```

### Color

```java
// Using predefined colors
component.withColor(Color.BLACK);

// Using RGB values
component.withColor(Color.rgb(100, 150, 200));

// Using hex values
component.withColor(Color.fromHex("#FF5733"));
```

### Line Spacing

```java
component.withLineSpacing(1.5f);  // 1.5 times normal spacing
component.withLineSpacing(2.0f);  // Double spacing
```

### Margins and Padding

```java
component.withMargins(Margin.builder()
    .withTop(10f)
    .withRight(20f)
    .withBottom(10f)
    .withLeft(20f)
    .build());
```

## Best Practices

1. **Use Appropriate Components**
   - Use `Paragraph` for general text blocks
   - Use `Heading` for section titles
   - Use `List` for enumerated items
   - Use `Text` for inline styling

2. **Consistent Styling**
   - Create reusable styles
   - Maintain consistent font usage
   - Use standard spacing

3. **Text Flow**
   - Consider page breaks
   - Use appropriate line spacing
   - Handle orphans and widows

4. **Accessibility**
   - Use proper heading levels
   - Maintain good contrast
   - Consider font size

## Examples

### Mixed Text Styles

```java
Paragraph paragraph = Paragraph.builder()
    .addText("This is ")
    .addBoldText("important ")
    .addItalicText("and emphasized ")
    .addText("information.")
    .withFont("Arial")
    .withSize(12f)
    .withColor(Color.BLACK)
    .withAlignment(TextAlignment.JUSTIFIED)
    .withLineSpacing(1.5f)
    .build();
```

### Nested Lists

```java
List list = List.builder()
    .withType(ListType.UNORDERED)
    .addItem("Main item 1")
    .addSubList(List.builder()
        .withType(ListType.ORDERED)
        .addItem("Sub item 1.1")
        .addItem("Sub item 1.2")
        .build())
    .addItem("Main item 2")
    .build();
```

### Complex Heading

```java
Heading heading = Heading.builder()
    .withText("Chapter 1: Introduction")
    .withLevel(1)
    .withFont(Font.builder()
        .withName("Georgia")
        .withSize(24f)
        .withStyle(FontStyle.BOLD)
        .build())
    .withColor(Color.rgb(0, 51, 102))
    .withAlignment(TextAlignment.CENTER)
    .withMargins(Margin.builder()
        .withTop(20f)
        .withBottom(10f)
        .build())
    .build();
```

## Next Steps

- Learn about specific text components:
  - [Paragraph](paragraph)
  - [Heading](heading)
  - [List](list)
  - [Text](text)
- See [Examples](../../examples) for more complex usage
- Read about [Layout Components](../layout) for structural elements
