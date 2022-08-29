use std::collections::HashMap;

use Datapoint::*;

pub enum Datapoint {
    Bool(bool),
    I8(i8),
    I16(i16),
    I32(i32),
    I64(i64),
    F32(f32),
    F64(f64),
    Str(String),
    Array(Vec<Datapoint>),
    Struct(HashMap<String, Datapoint>),
}

impl Datapoint {
    pub(crate) fn internal_data_string(&self, out: &mut Vec<String>, name: &String) {
        out.push(match self {
            // Implicit
            Bool(value) => format!("{} {} ", name, value),
            I32(value) => format!("{} {} ", name, value),
            F32(value) => {
                if value.ceil().eq(value) {
                    format!("{} {}.0 ", name, value)
                } else {
                    format!("{} {} ", name, value)
                }
            }
            Str(value) => {
                let v = value.replace("\t", "\\t")
                    .replace("\r", "\\r")
                    .replace("\n", "\\n")
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"");
                format!("{} \"{}\"", name, v)
            }

            // Explicit
            I8(value) => format!("{}:i8 {} ", name, value),
            I16(value) => format!("{}:i16 {} ", name, value),
            I64(value) => format!("{}:i64 {} ", name, value),
            F64(value) => format!("{}:f64 {} ", name, value),

            // Complex
            Array(value) => {}
            Struct(value) => {
                out.push(format!("{}{{", name));
                for (name, data) in value {
                    data.internal_data_string(out, name);
                }
                "}".to_string()
            }
        });
    }

    pub(crate) fn internal_format_string(&self, out: &mut Vec<String>, name: &String, depth: i32) {
        for _ in 0..depth {
            out.push("\t".to_string())
        }

        out.push(match self {
            // Implicit
            Bool(value) => format!("{} {}", name, value),
            I32(value) => format!("{} {}", name, value),
            F32(value) => {
                if value.ceil().eq(value) {
                    format!("{} {}.0", name, value)
                } else {
                    format!("{} {}", name, value)
                }
            }
            Str(value) => {
                let v = value.replace("\t", "\\t")
                    .replace("\r", "\\r")
                    .replace("\n", "\\n")
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"");
                format!("{} \"{}\"", name, v)
            }

            // Explicit
            I8(value) => format!("{}:i8 {}", name, value),
            I16(value) => format!("{}:i16 {}", name, value),
            I64(value) => format!("{}:i64 {}", name, value),
            F64(value) =>
                if value.ceil().eq(value) {
                    format!("{}:f64 {}.0", name, value)
                } else {
                    format!("{}:f64 {}", name, value)
                }

            // Complex
            Array(value) => {}
            Struct(value) => {
                out.push(format!("{} {{", name));
                for (name, data) in value {
                    data.internal_format_string(out, name, depth + 1);
                }
                "}".to_string()
            }
        });

        out.push("\n".to_string())
    }


    pub(crate) fn internal_pretty_string(&self, out: &mut Vec<String>, name: &String, depth: i32) {
        for _ in 0..depth {
            out.push("\t".to_string())
        }

        out.push(match self {
            Bool(value) => format!("{}: bool = {};", name, value),
            I8(value) => format!("{}: i8 = {};", name, value),
            I16(value) => format!("{}: i16 = {};", name, value),
            I32(value) => format!("{}: i32 = {};", name, value),
            I64(value) => format!("{}: i64 = {};", name, value),
            F32(value) => format!("{}: f32 = {};", name, value),
            F64(value) => format!("{}: f64 = {};", name, value),
            Str(value) => format!("{}: str = \"{}\";", name, value
                .replace("\t", "\\t")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")),

            // Complex
            Array(value) => {}
            Struct(value) => {
                out.push(format!("struct {} {{", name));
                for (name, data) in value {
                    data.internal_pretty_string(out, name, depth + 1);
                }
                "}".to_string()
            }
        });

        out.push("\n".to_string())
    }

    fn stringify_array_content_data(&self, out: &mut Vec<String>) {
        out.push("[".to_string());

        out.push(match self {
            // Implicit
            Bool(value) => format!("{},", value),
            I8(value) => format!("{},", value),
            I16(value) => format!("{},", value),
            I32(value) => format!("{},", value),
            I64(value) => format!("{},", value),
            F64(value) => format!("{},", value),
            F32(value) => {
                if value.ceil().eq(value) {
                    format!("{}.0,", value)
                } else {
                    format!("{},", value)
                }
            }
            Str(value) => format!("\"{}\",", value
                .replace("\t", "\\t")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")),

            // Complex
            Array(values) => {
                for value in values {
                    value.stringify_array_content_data(out);
                    out.push(",".to_string());
                }
                "".to_string()
            }
            Struct(_) => panic!("Structs in arrays are not permitted!"),
        });

        if let Some(v) = out.pop() {
            unsafe {
                out.push(v.get_unchecked(0..v.len() - 1).to_string());
                out.push("]".to_string());
            }
        }
    }

    fn stringify_array_content_format(&self, out: &mut Vec<String>, depth: i32) {
        for _ in 0..depth {
            out.push("\t".to_string())
        }

        out.push("[\n".to_string());

        out.push(match self {
            // Implicit
            Bool(value) => format!("{}, ", value),
            I8(value) => format!("{}, ", value),
            I16(value) => format!("{}, ", value),
            I32(value) => format!("{}, ", value),
            I64(value) => format!("{}, ", value),
            F64(value) => format!("{}, ", value),
            F32(value) => {
                if value.ceil().eq(value) {
                    format!("{}.0, ", value)
                } else {
                    format!("{}, ", value)
                }
            }
            Str(value) => format!("\"{}\", ", value
                .replace("\t", "\\t")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")),

            // Complex
            Array(values) => {
                for value in values {
                    value.stringify_array_content_format(out, depth + 1);
                    out.push(", ".to_string());
                }
                "".to_string()
            }
            Struct(_) => panic!("Structs in arrays are not permitted!"),
        });

        if let Some(v) = out.pop() {
            unsafe {
                out.push(v.get_unchecked(0..v.len() - 2).to_string());

                for _ in 0..depth - 1 {
                    out.push("\t".to_string())
                }

                out.push("]".to_string());
            }
        }
    }
}

impl Datapoint {
    pub fn add_to_array(&mut self, data: Datapoint) -> &mut Datapoint {
        match self {
            Array(ref mut vec) => vec.push(data),
            _ => eprintln!("Attempt to put datapoint in non-array datapoint!")
        }

        self
    }

    pub fn remove_from_array(&mut self, index: usize) -> &mut Datapoint {
        match self {
            Array(ref mut vec) => { vec.remove(index); }
            _ => eprintln!("Attempt to remove datapoint from non-array datapoint!")
        }

        self
    }

    pub fn add_to_struct(&mut self, name: String, data: Datapoint) -> &mut Datapoint {
        match self {
            Struct(ref mut map) => { map.insert(name, data); }
            _ => eprintln!("Attempt to put datapoint '{}' in non-struct datapoint!", name)
        }

        self
    }

    pub fn remove_from_struct(&mut self, name: &String) -> &mut Datapoint {
        match self {
            Struct(ref mut map) => { map.remove(name); }
            _ => eprintln!("Attempt to remove datapoint '{}' from non-struct datapoint!", name)
        }

        self
    }
}

pub trait Index {
    fn index(&mut self, index: usize) -> Option<&mut Datapoint>;
}

pub trait Find {
    fn find(&mut self, name: &String) -> Option<&mut Datapoint>;
}

impl Index for Datapoint {
    fn index(&mut self, index: usize) -> Option<&mut Datapoint> {
        return match self {
            Array(data) => Some(&mut data[index]),
            _ => None
        };
    }
}

impl Index for Option<&mut Datapoint> {
    fn index(&mut self, index: usize) -> Option<&mut Datapoint> {
        if let Some(array) = self {
            return match array {
                Array(data) => Some(&mut data[index]),
                _ => None
            };
        }
        None
    }
}

impl Find for Datapoint {
    fn find(&mut self, name: &String) -> Option<&mut Datapoint> {
        return match self {
            Struct(data) => data.get_mut(name),
            _ => None
        };
    }
}

impl Find for Option<&mut Datapoint> {
    fn find(&mut self, name: &String) -> Option<&mut Datapoint> {
        if let Some(struc) = self {
            return match struc {
                Struct(data) => data.get_mut(name),
                _ => None
            };
        }
        None
    }
}