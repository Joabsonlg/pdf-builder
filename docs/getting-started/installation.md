---
layout: default
title: Installation
parent: Getting Started
nav_order: 1
---

# Installation Guide
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## System Requirements

Before installing PDF Builder, ensure your system meets the following requirements:

### Java Version
- Java 17 or higher
- Both JDK and JRE are supported
- OpenJDK or Oracle JDK

### Build Tool
- Maven 3.x (recommended)
- Gradle 7.x or higher (alternative)

### Operating System
- Windows
- Linux
- macOS

## Maven Installation

### Add Repository

If you're using SNAPSHOT versions, add the Sonatype snapshot repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>sonatype-snapshots</id>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

### Add Dependency

Add the PDF Builder dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.joabsonlg</groupId>
    <artifactId>pdf-builder</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Gradle Installation

Add the dependency to your `build.gradle`:

```gradle
repositories {
    maven {
        url "https://s01.oss.sonatype.org/content/repositories/snapshots/"
    }
}

dependencies {
    implementation 'io.github.joabsonlg:pdf-builder:1.0-SNAPSHOT'
}
```

## Manual Installation

While not recommended, you can manually download and include the JAR file:

1. Download the latest JAR from [GitHub Releases](https://github.com/joabsonlg/pdf-builder/releases)
2. Add the JAR to your project's classpath
3. Add all required dependencies manually

## Verifying Installation

Create a simple test class to verify the installation:

```java
import io.github.joabsonlg.pdfbuilder.core.PDFBuilder;
import io.github.joabsonlg.pdfbuilder.components.text.Paragraph;

public class InstallTest {
    public static void main(String[] args) {
        PDFBuilder builder = new PDFBuilder();
        
        builder.addParagraph(Paragraph.builder()
            .addText("PDF Builder is working!")
            .build());
            
        builder.save("test.pdf");
        
        System.out.println("If you can see this message and a test.pdf file was created, the installation was successful!");
    }
}
```

## Troubleshooting

### Common Issues

#### Missing Dependencies
If you see `ClassNotFoundException` or `NoClassDefFoundError`, ensure all dependencies are properly included:
- Apache PDFBox
- Commons IO
- SLF4J

#### Java Version Mismatch
Error message containing `java.lang.UnsupportedClassVersionError`:
- Ensure you're using Java 17 or higher
- Check both compilation and runtime Java versions

#### Memory Issues
If you encounter `OutOfMemoryError`:
- Increase JVM heap size: `java -Xmx512m YourClass`
- Consider using streaming API for large documents

### Getting Help

If you encounter any issues:

1. Check the [FAQ](../faq) section
2. Search [existing issues](https://github.com/joabsonlg/pdf-builder/issues)
3. Create a new issue with:
   - Complete error message
   - Your environment details
   - Minimal reproducible example

## Next Steps

- Learn [Basic Concepts](basic-concepts)
- See [Quick Start Guide](quick-start)
- Explore [Examples](../examples)
