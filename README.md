# Alliance data notation
Version 1.0.0.2

An open source, strictly typed, JSON-like object notation. It is primarily designed for use in Alliance applications but
anyone who wants to use it or contribute is welcome to do so.

## Features:
- read file
- write file
- get and insert

## Supported types:

| ADN type | JVM type     | .Net type    | Rust type  | Inferred |
|----------|--------------|--------------|------------|----------|
| bool     | boolean      | bool         | bool       | true     |
| i8       | byte         | byte         | i8         | false    |
| i16      | short        | short        | i16        | false    |
| i32      | int          | int          | i32        | true     |
| i64      | long         | long         | i64        | false    |
| f32      | float        | float        | f32        | true     |
| f64      | double       | double       | f64        | false    |
| str      | String       | string       | str        | true     |
| []       | ArrayList<T> | ArrayList<T> | HashSet<T> | -        |
| struct   | HashSet<T>   | HashSet<T>   | HashSet<T> | -        |

## Syntax:
### Naming:
Names may contain uppercase letters (A..Z), lowercase letters (a..z), numbers (0..9) or underscores ( _ ). The names *MUST* start with a letter in either case though. 

### Inferrable types:
- bool: `true` or `false`
- i32: whole numbers numbers (e.g. `12`)
- f32: decimal numbers numbers (e.g. `7.25`)
- str: quoted strings (e.g. `"Hello, world!"`)
- array: inferred from contained values (mixtures of decimal and whole numbers result in f32 being inferred)

### Literals
Explicitly typed:<br>
`<name>: <type> = <value>;`

Type inferred:<br>
`<name> = <value>;`

### Arrays
Explicitly typed:<br>
`<name>: <type> = <value>;`

Type inferred:<br>
`<name> = <value>;`


Inferred type:
``
- Objects:  `struct <name> { ... }`
- Arrays:   `<name> [ ... ]`
- Arrays (type inferred): `<name> [ ... ]`
- Multi dimensional arrays: `<name>: <type>[][] `
- Literals: `<type> <name> = <value>;`
- Inferred literal: 

## Roadmap:
- Modes: Simple (types: number, string, object), Expressive
- Casting levels: none, smart, any
- new type: timestamp YYYY-MM-DDTHH:MM:SS