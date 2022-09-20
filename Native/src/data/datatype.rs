use std::collections::HashMap;

use crate::data::Datapoint;
use crate::data::datatype::DataType::*;

#[derive(Debug, Clone)]
pub enum DataType {
    Bool,
    I8,
    I16,
    I32,
    I64,
    F32,
    F64,
    Str,
    Array(Box<DataType>),
}

impl DataType {
    /// This method either returns:
    /// - self: if the target is None
    /// - target: if the target is castable to
    /// - None: if the target is not castable to
    /// Bool may be cast to I8, I16, I32, I64 (value 1 or 0) and Str ("true", "false")
    /// I8 may be cast to I16, I32, I64, F32, F64 and Str
    /// I16 may be cast to I32, I64, F32, F64 and Str
    /// I32 may be cast to I64, F64 and Str
    /// I64 may be cast to Str
    /// F32 may be cast to F64 and Str
    /// F64 may be cast to Str
    /// Str and Arrays may NOT be cast.
    pub fn is_castable_from(&self, target: &Option<DataType>) -> Option<DataType> {
        if let Some(other) = target {
            return match self {
                Bool => match other {
                    F32 | F64 | Str | Array(_) => None,
                    Bool => Some(Bool),
                    I8 => Some(I8),
                    I16 => Some(I16),
                    I32 => Some(I32),
                    I64 => Some(I64),
                },
                I8 => match other {
                    Bool | Array(_) => None,
                    I8 => Some(I8),
                    I16 => Some(I16),
                    I32 => Some(I32),
                    I64 => Some(I64),
                    F32 => Some(F32),
                    F64 => Some(F64),
                    Str => Some(Str),
                },
                I16 => match other {
                    Bool | I8 | Array(_) => None,
                    I16 => Some(I16),
                    I32 => Some(I32),
                    I64 => Some(I64),
                    F32 => Some(F32),
                    F64 => Some(F64),
                    Str => Some(Str),
                },
                I32 => match other {
                    Bool | I8 | I16 | F32 | Array(_) => None,
                    I32 => Some(I32),
                    I64 => Some(I64),
                    F64 => Some(F64),
                    Str => Some(Str),
                },
                I64 => match other {
                    Bool | I8 | I16 | I32 | F32 | F64 | Array(_) => None,
                    I64 => Some(I64),
                    Str => Some(Str),
                },
                F32 => match other {
                    Bool | I8 | I16 | I32 | I64 | Array(_) => None,
                    F32 => Some(F32),
                    F64 => Some(F64),
                    Str => Some(Str),
                },
                F64 => match other {
                    Bool | I8 | I16 | I32 | I64 | F32 | Array(_) => None,
                    F64 => Some(F64),
                    Str => Some(Str),
                },
                Str => if matches!(other, Str) {
                    Some(Str)
                } else {
                    None
                },
                Array(own) => if let Array(oth) = other {
                    own.is_castable_from(&Some(*(oth.clone())))
                } else {
                    None
                },
            };
        }

        Some(self.clone())
    }
}

pub trait Datatype<T: Datatype = Self> {
    const DATATYPE: DataType = Bool;
}

impl Datatype for bool {
    const DATATYPE: DataType = Bool;
}

impl Datatype for i8 {
    const DATATYPE: DataType = I8;
}

impl Datatype for i16 {
    const DATATYPE: DataType = I16;
}

impl Datatype for i32 {
    const DATATYPE: DataType = I32;
}

impl Datatype for i64 {
    const DATATYPE: DataType = I64;
}

impl Datatype for f32 {
    const DATATYPE: DataType = F32;
}

impl Datatype for f64 {
    const DATATYPE: DataType = F64;
}

impl Datatype for String {
    const DATATYPE: DataType = Str;
}

impl Datatype for Vec<Datapoint> {
    const DATATYPE: DataType = Array(Self.first().unwrap_or(&I8).);
}

impl Datatype for HashMap<String, Datapoint> {}