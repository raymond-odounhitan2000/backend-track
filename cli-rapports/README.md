# CLI Rapports

CLI tool to generate sales reports from CSV files, with support for multiple output formats.

Built with Java 21 (records, pattern matching, streams) and [Picocli](https://picocli.info/).

## Requirements

- Java 21 or higher
- Maven 3.6+

## Build

```bash
mvn clean package
```

This produces an executable fat JAR at `target/cli-rapports-1.0-SNAPSHOT.jar`.

## Usage

```bash
java -jar target/cli-rapports-1.0-SNAPSHOT.jar \
    --input <input-file.csv> \
    --output <output-file> \
    --format <CSV|MARKDOWN|TEXT>
```

### Options

| Option | Short | Description |
|--------|-------|-------------|
| `--input` | `-i` | Input CSV file path |
| `--output` | `-o` | Output file path |
| `--format` | `-f` | Output format: `CSV`, `MARKDOWN`, or `TEXT` |
| `--help` | `-h` | Show help |
| `--version` | `-V` | Show version |

### Input format

The input CSV file must have the following header:

```csv
product,region,unitPrice,quantity,date
```

Example:

```csv
product,region,unitPrice,quantity,date
Laptop,Cotonou,500000.00,2,2026-03-15
Smartphone,Porto-Novo,250000.00,5,2026-03-15
```

Dates must follow the ISO 8601 format (`YYYY-MM-DD`).

## Examples

### Generate a Markdown report

```bash
java -jar target/cli-rapports-1.0-SNAPSHOT.jar \
    -i data/sales.csv \
    -o report.md \
    -f MARKDOWN
```

### Generate a CSV report

```bash
java -jar target/cli-rapports-1.0-SNAPSHOT.jar \
    -i data/sales.csv \
    -o report.csv \
    -f CSV
```

### Generate a plain text report

```bash
java -jar target/cli-rapports-1.0-SNAPSHOT.jar \
    -i data/sales.csv \
    -o report.txt \
    -f TEXT
```

## Output

The tool aggregates sales by product and computes:

- **Total quantity** sold for each product
- **Total amount** (sum of `unitPrice × quantity`)

Results are sorted by total amount in descending order.

## Exit codes

| Code | Meaning |
|------|---------|
| `0` | Success |
| `1` | I/O error (file not found, write failure...) |
| `2` | Unexpected error |

## Testing

Run the unit test suite:

```bash
mvn test
```

The project includes 8 unit tests covering:

- `ReportBuilder` aggregation logic (5 tests)
- `SalesReader` CSV parsing (1 test)
- `CsvFormatter` output (1 test)
- `MarkdownFormatter` output (1 test)

## Performance benchmark

A test data generator is included to benchmark performance on larger datasets.

### Generate a test dataset

```bash
mvn compile exec:java -Dexec.mainClass="org.itnum.tools.SalesGenerator"
```

By default, this generates `data/sales-10k.csv` with 10,000 lines.

You can customize the number of lines and the output file:

```bash
mvn compile exec:java \
    -Dexec.mainClass="org.itnum.tools.SalesGenerator" \
    -Dexec.args="50000 data/sales-50k.csv"
```

### Benchmark the report generation

```bash
time java -jar target/cli-rapports-1.0-SNAPSHOT.jar \
    -i data/sales-10k.csv \
    -o report-10k.md \
    -f MARKDOWN
```

Typical run: **< 1 second** for 10,000 lines on standard hardware.

## Project structure

```
src/main/java/org/itnum/
├── Main.java                       # Entry point
├── cli/ReportCommand.java          # Picocli command + orchestration
├── exception/CliException.java     # Business exception
├── format/Format.java              # Output format enum (CSV, MARKDOWN, TEXT)
├── model/
│   ├── Sale.java                   # Sale record
│   └── ProductReport.java          # Aggregated report record
├── parser/SalesReader.java         # CSV parser
├── tools/SalesGenerator.java       # Test data generator (benchmark)
├── transform/ReportBuilder.java    # Aggregation logic
└── writer/
    ├── ReportFormatter.java        # Strategy interface
    ├── CsvFormatter.java
    ├── MarkdownFormatter.java
    ├── TextFormatter.java
    └── ReportWriter.java           # File writer
```