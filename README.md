# Alliance data notation
Version 1.0.0.2

An open source, strictly typed, JSON-like object notation. It is primarily designed for use in Alliance applications but
anyone who wants to use it or contribute is welcome to do so.

# Features:
- read file
- write file
- get and insert

# Supported types:

| ADN type | JVM type     | .Net type    | Rust type  |
|----------|--------------|--------------|------------|
| bool     | boolean      | bool         | bool       |
| i8       | byte         | byte         | i8         |
| i16      | short        | short        | i16        |
| i32      | int          | int          | i32        |
| i64      | long         | long         | i64        |
| f32      | float        | float        | f32        |
| f64      | double       | double       | f64        |
| str      | String       | string       | str        |
| []       | ArrayList<T> | ArrayList<T> | HashSet<T> |
| struct   | HashSet<T>   | HashSet<T>   | HashSet<T> |

# Roadmap:
- Modes: Simple (types: number, string, object), Expressive
- Casting levels: none, smart, any
- new type: timestamp YYYY-MM-DDTHH:MM:SS