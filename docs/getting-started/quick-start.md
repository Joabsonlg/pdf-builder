---
layout: default
title: Quick Start
parent: Getting Started
nav_order: 3
---

# Quick Start Guide
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Your First PDF Document

Let's create a simple PDF document with a heading, some text, and an image.

### 1. Add Dependency

First, add PDF Builder to your project:

Maven:
```xml
<dependency>
    <groupId>io.github.joabsonlg</groupId>
    <artifactId>pdf-builder</artifactId>
    <version>1.0.0</version>
</dependency>
```

Gradle:
```groovy
implementation 'io.github.joabsonlg:pdf-builder:1.0.0'
```

### 2. Create a Simple Document

Here's a complete example that creates a basic PDF:

```java
import io.github.joabsonlg.pdfbuilder.PDFBuilder;
import io.github.joabsonlg.pdfbuilder.config.PDFConfiguration;
import io.github.joabsonlg.pdfbuilder.component.*;

public class QuickStart {
    public static void main(String[] args) {
        // Create configuration
        PDFConfiguration config = PDFConfiguration.create()
            .withPageSize(PDRectangle.A4)
            .withMargins(50f)
            .build();

        // Initialize builder
        PDFBuilder builder = new PDFBuilder(config);

        // Add content
        builder.addHeading("Welcome to PDF Builder!")
              .addParagraph("This is a simple example showing how to create a PDF document.")
              .addImage("path/to/image.png")
              .addParagraph("The image above was automatically scaled to fit the page width.");

        // Save the document
        builder.save("quick-start.pdf");
    }
}
```

### 3. Add Some Style

Let's enhance the document with some styling:

```java
import io.github.joabsonlg.pdfbuilder.style.*;

public class StyledQuickStart {
    public static void main(String[] args) {
        // Create styled heading
        Heading heading = Heading.builder()
            .withText("Welcome to PDF Builder!")
            .withColor(Color.BLUE)
            .withFontSize(24f)
            .withAlignment(TextAlignment.CENTER)
            .build();

        // Create styled paragraph
        Paragraph paragraph = Paragraph.builder()
            .addText("This is a ")
            .addBoldText("styled")
            .addText(" example showing how to create a ")
            .addItalicText("beautiful")
            .addText(" PDF document.")
            .withLineSpacing(1.5f)
            .build();

        // Create styled image
        Image image = Image.builder()
            .withPath("path/to/image.png")
            .withCaption("Figure 1: Example Image")
            .withWidth(300f)
            .withAlignment(ImageAlignment.CENTER)
            .build();

        // Build the document
        PDFBuilder builder = new PDFBuilder();
        builder.add(heading)
               .add(paragraph)
               .add(image)
               .save("styled-quick-start.pdf");
    }
}
```

## Adding Tables

Tables are a great way to organize data:

```java
// Create table
Table table = Table.builder()
    .withHeaders("Name", "Age", "City")
    .addRow("John Doe", "30", "New York")
    .addRow("Jane Smith", "25", "London")
    .addRow("Bob Johnson", "35", "Paris")
    .withHeaderBackground(Color.DARK_GRAY)
    .withHeaderTextColor(Color.WHITE)
    .withBorders(true)
    .build();

builder.add(table);
```

## Working with Lists

Add ordered and unordered lists:

```java
// Unordered list
List unorderedList = List.builder()
    .withType(ListType.UNORDERED)
    .addItem("First item")
    .addItem("Second item")
    .addItem("Third item")
    .build();

// Ordered list
List orderedList = List.builder()
    .withType(ListType.ORDERED)
    .addItem("Step one")
    .addItem("Step two")
    .addItem("Step three")
    .build();

builder.add(unorderedList)
       .add(orderedList);
```

## Headers and Footers

Add professional headers and footers:

```java
// Configure header
HeaderConfig header = HeaderConfig.builder()
    .withLogo("path/to/logo.png")
    .withText("Company Name")
    .withAlignment(TextAlignment.RIGHT)
    .build();

// Configure footer
FooterConfig footer = FooterConfig.builder()
    .withPageNumbers(true)
    .withText("Confidential")
    .withAlignment(TextAlignment.CENTER)
    .build();

// Apply to configuration
PDFConfiguration config = PDFConfiguration.create()
    .withHeader(header)
    .withFooter(footer)
    .build();
```

## Complete Example

Here's a complete example that puts it all together:

```java
import io.github.joabsonlg.pdfbuilder.*;
import io.github.joabsonlg.pdfbuilder.component.*;
import io.github.joabsonlg.pdfbuilder.style.*;

public class CompleteExample {
    public static void main(String[] args) {
        // Configuration
        PDFConfiguration config = PDFConfiguration.create()
            .withPageSize(PDRectangle.A4)
            .withMargins(50f)
            .withHeader(HeaderConfig.builder()
                .withLogo("logo.png")
                .withText("PDF Builder Example")
                .build())
            .withFooter(FooterConfig.builder()
                .withPageNumbers(true)
                .build())
            .build();

        // Create builder
        PDFBuilder builder = new PDFBuilder(config);

        // Add content
        builder.add(Heading.builder()
                .withText("Project Report")
                .withLevel(1)
                .build())
            .add(Paragraph.builder()
                .addText("This report was generated on ")
                .addBoldText(new Date().toString())
                .build())
            .add(Table.builder()
                .withHeaders("Metric", "Value", "Status")
                .addRow("Performance", "95%", "✓")
                .addRow("Quality", "98%", "✓")
                .addRow("Delivery", "92%", "!")
                .build())
            .add(List.builder()
                .withType(ListType.UNORDERED)
                .addItem("Performance metrics exceeded targets")
                .addItem("Quality standards met consistently")
                .addItem("Delivery times need improvement")
                .build())
            .add(Image.builder()
                .withPath("chart.png")
                .withCaption("Figure 1: Performance Metrics")
                .build());

        // Save document
        builder.save("complete-example.pdf");
    }
}
```

## Next Steps

Now that you've created your first PDF document, you can:

1. Learn about [Configuration Options](configuration)
2. Explore available [Components](../components)
3. See more [Examples](../examples)
4. Read about [Advanced Features](../advanced-features)
