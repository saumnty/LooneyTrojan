# LooneyAV (Antivirus Prototype)

LooneyAV is a **desktop antivirus prototype** developed in Java, designed as an academic proof-of-concept to explore file scanning, malware detection logic, and user-oriented security features.

## Features
- Desktop application built with **JavaFX**, including a multi-view interface (scan, quarantine, profile, settings).
- **Basic and full system scans** with progress tracking and user cancellation support.
- **Signature-based detection prototype**, using a dictionary of known file name patterns.
- **Quarantine system** that relocates detected files and records their original paths.
- Local persistence using **SQLite (JDBC)** for users, plans, and malware signatures.
- **Internationalization (i18n)** using Java ResourceBundle (Spanish and English).
- JSP-based landing website that detects client OS and architecture to suggest appropriate downloads.

## Technologies
- Java
- JavaFX
- JDBC
- SQLite
- JSP / HTML / CSS
- File I/O and NIO

## Project Scope
This project is intended as an **educational prototype**. The malware detection mechanism is dictionary-based and does not include heuristic or behavioral analysis.

## How to Run
1. Open the project in NetBeans.
2. Ensure Java (JDK 8+) is installed.
3. Run the JavaFX application.
4. SQLite databases are created automatically on first run.

## Screenshots
(Add screenshots here)

## Future Improvements
- Hash-based and heuristic detection.
- External signature updates.
- Configuration via environment variables.
- Improved separation between UI and data access layers.

## Author
Santiago Saucedo Mendoza