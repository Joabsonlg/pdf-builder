---
layout: default
title: Components
nav_order: 3
has_children: true
---

# Components
{: .no_toc }

PDF Builder provides a rich set of components to help you create beautiful and functional PDF documents. Each component is designed to be:

- Easy to use with a fluent builder API
- Highly customizable
- Composable with other components
- Type-safe and maintainable

## Component Categories

### Text Components
- [Paragraph](text/paragraph) - Basic text blocks with rich formatting
- [Heading](text/heading) - Section headers with multiple levels
- [List](text/list) - Ordered and unordered lists
- [Text](text/text) - Inline text with styling

### Layout Components
- [Table](layout/table) - Data organization in rows and columns
- [Grid](layout/grid) - Flexible grid-based layouts
- [Section](layout/section) - Logical document sections
- [Column](layout/column) - Multi-column layouts

### Media Components
- [Image](media/image) - Images with captions and scaling
- [Logo](media/logo) - Company logos and branding
- [Chart](media/chart) - Data visualization
- [Barcode](media/barcode) - Various barcode formats

### Form Components
- [TextField](form/textfield) - User input fields
- [Checkbox](form/checkbox) - Boolean selections
- [RadioButton](form/radiobutton) - Single selections from options
- [Signature](form/signature) - Digital signatures

### Decoration Components
- [Line](decoration/line) - Horizontal and vertical lines
- [Border](decoration/border) - Borders around content
- [Background](decoration/background) - Colored backgrounds
- [Watermark](decoration/watermark) - Text or image watermarks

### Navigation Components
- [Link](navigation/link) - Internal and external links
- [Bookmark](navigation/bookmark) - PDF bookmarks
- [TableOfContents](navigation/toc) - Automatic table of contents
- [Index](navigation/index) - Keyword index

## Component Principles

1. **Consistency**
   - All components follow the same builder pattern
   - Common properties are named consistently
   - Similar behavior across components

2. **Composability**
   - Components can be nested
   - Properties can be inherited
   - Styles can be shared

3. **Flexibility**
   - Multiple ways to configure
   - Sensible defaults
   - Override capabilities

4. **Performance**
   - Lazy rendering
   - Resource management
   - Memory efficiency

## Example Usage

Here's a quick example showing how different components work together:

```java
Document document = Document.builder()
    .add(Heading.builder()
        .withText("Annual Report")
        .withLevel(1)
        .build())
    .add(Paragraph.builder()
        .addText("Financial highlights for ")
        .addBoldText("2024")
        .build())
    .add(Table.builder()
        .withHeaders("Quarter", "Revenue", "Growth")
        .addRow("Q1", "$1M", "+10%")
        .addRow("Q2", "$1.2M", "+20%")
        .build())
    .add(Image.builder()
        .withPath("chart.png")
        .withCaption("Revenue Growth")
        .build())
    .build();
```

## Best Practices

1. **Use Builders**
   - Always use the builder pattern
   - Chain methods for readability
   - Build once, reuse where possible

2. **Style Management**
   - Create reusable styles
   - Use style inheritance
   - Maintain consistency

3. **Resource Handling**
   - Close resources properly
   - Use try-with-resources
   - Manage memory efficiently

4. **Error Handling**
   - Validate input early
   - Provide meaningful errors
   - Handle exceptions appropriately

## Next Steps

- Learn about specific components in their respective sections
- See [Examples](../examples) for more complex usage
- Read about [Advanced Features](../advanced-features)
- Check [Best Practices](../best-practices) for guidelines
