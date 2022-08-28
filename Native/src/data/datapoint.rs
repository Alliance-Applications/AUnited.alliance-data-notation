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
    pub(crate) fn internal_data_string(&self, p0: &mut Vec<String>, p1: String) {
        todo!()
    }

    pub(crate) fn internal_format_string(&self, p0: &mut Vec<String>, p1: String) {
        todo!()
    }


    pub(crate) fn internal_pretty_string(&self, p0: &mut Vec<String>, p1: String) {
        todo!()
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