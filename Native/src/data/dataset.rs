use std::collections::HashMap;
use std::fs::File;
use std::io::{Error, ErrorKind, Read, Write};
use std::ops::RemAssign;
use std::path::Path;

use crate::data::{Datapoint, NotationFormat};
use crate::semantic::Lexer;
use crate::syntactic::Parser;

pub struct Dataset {
    content: HashMap<String, Datapoint>,
    format: NotationFormat,
}

pub trait SearchSet {
    fn find(&mut self, name: &String) -> Option<&mut Datapoint>;
}

impl SearchSet for Dataset {
    fn find(&mut self, name: &String) -> Option<&mut Datapoint> {
        self.content.get_mut(name)
    }
}

impl SearchSet for Option<Dataset> {
    fn find(&mut self, name: &String) -> Option<&mut Datapoint> {
        match self {
            Some(ref mut set) => set.content.get_mut(name),
            _ => None
        }
    }
}

impl Default for Dataset {
    fn default() -> Self {
        Dataset::new()
    }
}

impl Dataset {
    pub fn new() -> Self {
        Dataset {
            content: HashMap::new(),
            format: NotationFormat::Data,
        }
    }

    pub fn add(&mut self, name: String, data: Datapoint) -> &mut Self {
        self.content.insert(name, data);
        self
    }

    pub fn remove(&mut self, name: &String) -> &mut Self {
        self.content.remove(name);
        self
    }

    pub fn set_format(&mut self, format: NotationFormat) -> &mut Self {
        self.format = format;
        self
    }

    pub fn read_file<P: AsRef<Path>>(path: P) -> Result<Dataset, Error> {
        let mut str = String::new();
        File::open(path)?.read_to_string(&mut str)?;
        Self::read_string(str)
    }

    pub fn read<R: Read>(reader: &mut R) -> Result<Dataset, Error> {
        let mut str = String::new();
        reader.read_to_string(&mut str)?;
        Self::read_string(str)
    }

    pub fn read_string(string: String) -> Result<Dataset, Error> {
        match Parser::parse(Lexer::lex(string)) {
            Err(error) => {
                Err(Error::new(ErrorKind::InvalidInput, error))
            }
            Ok(ok) => Ok(ok)
        }
    }

    pub fn write_file<P: AsRef<Path>>(&mut self, path: P) -> Result<(), Error> {
        let str = self.write_string();
        File::create(path)?.write_all(str.as_bytes())
    }

    pub fn write<W: Write>(&mut self, writer: &mut W) -> Result<(), Error> {
        let str = self.write_string();
        writer.write_all(str.as_bytes())
    }

    pub fn write_string(&mut self) -> String {
        let mut parts: Vec<String> = Vec::new();
        match self.format {
            NotationFormat::Data => {
                for (name, data) in self.content.iter() {
                    data.internal_data_string(&mut parts, name);
                }
            }
            NotationFormat::Formatted => {
                for (name, data) in self.content.iter() {
                    data.internal_format_string(&mut parts, name, 0);
                }
            }
            NotationFormat::Pretty => {
                for (name, data) in self.content.iter() {
                    data.internal_pretty_string(&mut parts, name, 0);
                }
            }
        }

        parts.join("")
    }

    pub fn merge(&mut self, other: Dataset) {
        for (name, data) in other.content {
            self.content.insert(name, data);
        }
    }
}

impl RemAssign for Dataset {
    #[inline]
    fn rem_assign(&mut self, rhs: Self) {
        self.merge(rhs);
    }
}