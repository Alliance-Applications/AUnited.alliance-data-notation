use std::collections::HashMap;

use Datapoint::*;

use crate::data::datatype::{DataType, Datatype};

pub enum Datapoint {
    Bool(bool),
    I8(i8),
    I16(i16),
    I32(i32),
    I64(i64),
    F32(f32),
    F64(f64),
    Str(String),
    Array(Box<DataType>, Vec<Datapoint>),
    Struct(HashMap<String, Datapoint>),
}

impl Datapoint {
    pub(crate) fn internal_data_string(&self, out: &mut Vec<String>, name: &String) {
        match self {
            // Implicit
            Bool(value) => out.push(format!("{} {} ", name, value)),
            I32(value) => out.push(format!("{} {} ", name, value)),
            F32(value) => if value.ceil().eq(value) {
                out.push(format!("{} {}.0 ", name, value));
            } else {
                out.push(format!("{} {} ", name, value));
            }
            Str(value) => out.push(format!("{} \"{}\"", name, value
                .replace('\t', "\\t")
                .replace('\r', "\\r")
                .replace('\n', "\\n")
                .replace('\\', "\\\\")
                .replace('\"', "\\\""))),

            // Explicit
            I8(value) => out.push(format!("{}:i8 {} ", name, value)),
            I16(value) => out.push(format!("{}:i16 {} ", name, value)),
            I64(value) => out.push(format!("{}:i64 {} ", name, value)),
            F64(value) => out.push(format!("{}:f64 {} ", name, value)),

            // Complex
            Array(datatype, _) => {
                let (text, implicit) = datatype.get_keyword();

                if implicit {
                    out.push(name.to_string());
                } else {
                    out.push(format!("{}:{}", name, text));
                };

                self.stringify_array_content_data(out);
            }
            Struct(value) => {
                out.push(format!("{}{{", name));

                for (name, data) in value {
                    data.internal_data_string(out, name);
                }

                out.push("}".to_string());
            }
        };
    }

    pub(crate) fn internal_format_string(&self, out: &mut Vec<String>, name: &String, depth: i32) {
        for _ in 0..depth {
            out.push("\t".to_string())
        }

        match self {
            // Implicit
            Bool(value) => out.push(format!("{} {}", name, value)),
            I32(value) => out.push(format!("{} {}", name, value)),
            F32(value) => if value.ceil().eq(value) {
                out.push(format!("{} {}.0", name, value));
            } else {
                out.push(format!("{} {}", name, value));
            },
            Str(value) => out.push(format!("{} \"{}\"", name, value
                .replace('\t', "\\t")
                .replace('\r', "\\r")
                .replace('\n', "\\n")
                .replace('\\', "\\\\")
                .replace('\"', "\\\""))),

            // Explicit
            I8(value) => out.push(format!("{}:i8 {}", name, value)),
            I16(value) => out.push(format!("{}:i16 {}", name, value)),
            I64(value) => out.push(format!("{}:i64 {}", name, value)),
            F64(value) => if value.ceil().eq(value) {
                out.push(format!("{}:f64 {}.0", name, value));
            } else {
                out.push(format!("{}:f64 {}", name, value));
            }

            // Complex
            Array(datatype, _) => {
                let (text, implicit) = datatype.get_keyword();

                if implicit {
                    out.push(format!("{} ", name));
                } else {
                    out.push(format!("{}: {} ", name, text));
                };

                self.stringify_array_content_format(out, depth + 1);
            }
            Struct(value) => {
                out.push(format!("{} {{", name));

                for (name, data) in value {
                    data.internal_format_string(out, name, depth + 1);
                }

                out.push("}".to_string());
            }
        };

        out.push("\n".to_string())
    }


    pub(crate) fn internal_pretty_string(&self, out: &mut Vec<String>, name: &String, depth: i32) {
        for _ in 0..depth {
            out.push("\t".to_string())
        }

        match self {
            Bool(value) => out.push(format!("{}: bool = {};", name, value)),
            I8(value) => out.push(format!("{}: i8 = {};", name, value)),
            I16(value) => out.push(format!("{}: i16 = {};", name, value)),
            I32(value) => out.push(format!("{}: i32 = {};", name, value)),
            I64(value) => out.push(format!("{}: i64 = {};", name, value)),
            F32(value) => out.push(format!("{}: f32 = {};", name, value)),
            F64(value) => out.push(format!("{}: f64 = {};", name, value)),
            Str(value) => out.push(format!("{}: str = \"{}\";", name, value
                .replace('\t', "\\t")
                .replace('\r', "\\r")
                .replace('\n', "\\n")
                .replace('\\', "\\\\")
                .replace('\"', "\\\""))),

            // Complex
            Array(datatype, _) => {
                out.push(format!("{}: {} ", name, datatype.get_keyword().0));
                self.stringify_array_content_format(out, depth + 1);
            }
            Struct(value) => {
                out.push(format!("struct {} {{", name));

                for (name, data) in value {
                    data.internal_pretty_string(out, name, depth + 1);
                }

                out.push("}".to_string());
            }
        };

        out.push("\n".to_string())
    }

    fn get_keyword(&self) -> (&str, bool) {
        return match self {
            Bool(_) => ("bool", true),
            I8(_) => ("i8", false),
            I16(_) => ("i16", false),
            I32(_) => ("i32", true),
            I64(_) => ("i64", false),
            F32(_) => ("f32", true),
            F64(_) => ("f64", false),
            Str(_) => ("str", true),
            Array(n, _) => n.get_keyword(),
            Struct(_) => ("struct", false),
        }
    }

    fn stringify_array_content_data(&self, out: &mut Vec<String>) {
        match self {
            // Implicit
            Bool(value) => out.push(format!("{}", value)),
            I8(value) => out.push(format!("{}", value)),
            I16(value) => out.push(format!("{}", value)),
            I32(value) => out.push(format!("{}", value)),
            I64(value) => out.push(format!("{}", value)),
            F64(value) => out.push(format!("{}", value)),

            F32(value) => if value.ceil().eq(value) {
                out.push(format!("{}.0", value));
            } else {
                out.push(format!("{}", value));
            },
            Str(value) => out.push(format!("\"{}\"", value
                .replace('\t', "\\t")
                .replace('\r', "\\r")
                .replace('\n', "\\n")
                .replace('\\', "\\\\")
                .replace('\"', "\\\""))),

            // Complex
            Array(_, values) => {
                out.push("[".to_string());

                for value in values {
                    value.stringify_array_content_data(out);
                    out.push(",".to_string());
                }

                if let Some(v) = out.pop() {
                    unsafe {
                        out.push(v.get_unchecked(0..v.len() - 1).to_string());
                        out.push("]".to_string());
                    }
                }
            }

            Struct(_) => panic!("Structs in arrays are not permitted!"),
        };
    }

    fn stringify_array_content_format(&self, out: &mut Vec<String>, depth: i32) {
        match self {
            // Implicit
            Bool(value) => out.push(format!("{}, ", value)),
            I8(value) => out.push(format!("{}, ", value)),
            I16(value) => out.push(format!("{}, ", value)),
            I32(value) => out.push(format!("{}, ", value)),
            I64(value) => out.push(format!("{}, ", value)),
            F64(value) => out.push(format!("{}, ", value)),
            F32(value) => {
                if value.ceil().eq(value) {
                    out.push(format!("{}.0, ", value));
                } else {
                    out.push(format!("{}, ", value));
                }
            }
            Str(value) => out.push(format!("\"{}\", ", value
                .replace('\t', "\\t")
                .replace('\r', "\\r")
                .replace('\n', "\\n")
                .replace('\\', "\\\\")
                .replace('\"', "\\\""))),

            // Complex
            Array(_, values) => {
                for _ in 0..depth {
                    out.push("\t".to_string())
                }

                out.push("[\n".to_string());

                for value in values {
                    value.stringify_array_content_format(out, depth + 1);
                    out.push(", ".to_string());
                }

                if let Some(v) = out.pop() {
                    unsafe {
                        out.push(v.get_unchecked(0..v.len() - 2).to_string());

                        for _ in 0..depth - 1 {
                            out.push("\t".to_string())
                        }

                        out.push("]".to_string());
                    }
                }
            },
            Struct(_) => panic!("Structs in arrays are not permitted!"),
        };
    }
}

impl Datapoint {
    pub fn add_to_array(&mut self, data: Datapoint) -> &mut Datapoint {
        match self {
            Array(_, ref mut vec) => vec.push(data),
            _ => eprintln!("Attempt to put datapoint in non-array datapoint!")
        }

        self
    }

    pub fn remove_from_array(&mut self, index: usize) -> &mut Datapoint {
        match self {
            Array(_, ref mut vec) => { vec.remove(index); }
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
    fn find(&mut self, name: &str) -> Option<&mut Datapoint>;
}

impl Index for Datapoint {
    fn index(&mut self, index: usize) -> Option<&mut Datapoint> {
        match self {
            Array(_, data) => Some(&mut data[index]),
            _ => None
        }
    }
}

impl Index for Option<&mut Datapoint> {
    fn index(&mut self, index: usize) -> Option<&mut Datapoint> {
        if let Some(array) = self {
            return match array {
                Array(_, data) => Some(&mut data[index]),
                _ => None
            };
        }
        None
    }
}

impl Find for Datapoint {
    fn find(&mut self, name: &str) -> Option<&mut Datapoint> {
        return match self {
            Struct(data) => data.get_mut(name),
            _ => None
        };
    }
}

impl Find for Option<&mut Datapoint> {
    fn find(&mut self, name: &str) -> Option<&mut Datapoint> {
        if let Some(struc) = self {
            return match struc {
                Struct(data) => data.get_mut(name),
                _ => None
            };
        }
        None
    }
}

type DataPoint<T: Datapointn> = T;

union Datatypes {
    bool: bool,
    i8: i8,
    bool: bool,
    bool: bool,
    bool: bool,
    bool: bool,
    bool: bool,
    bool: bool,
    bool: bool,
    bool: bool,
}

pub trait Datapointn<T: Datatype = Self>: Datatype<T> {
    fn cast(&self, target: DataType) -> Option<DataPoint<Self>>;
}