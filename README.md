# JOURN

Application dedicated to terminal-based daily journal management.

## Commands

`journ today`

Opens today's journal file; creates it if it does not exist.

`journ keywords (list (-s [0-9]+) (-n [0-9]+)|search [word])`

Self-explanatory commands for keyword management.

`journ open (help|list (-n [0-9]+)|MM-DD-YY [-c])`

`help` explains use of the `open` command.
`list` lists the `-n` most recent entries' dates. `MM-DD-YY` opens the described date's notes file if it exists; fails otherwise unless the `-c` parameter is provided.