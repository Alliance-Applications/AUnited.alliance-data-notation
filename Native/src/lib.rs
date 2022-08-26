pub(crate) use io::*;
pub(crate) use walkable::*;

pub mod data {
    pub use element::*;
    pub use element_array::*;
    pub use element_bool::*;
    pub use element_f32::*;
    pub use element_f64::*;
    pub use element_i16::*;
    pub use element_i32::*;
    pub use element_i64::*;
    pub use element_i8::*;
    pub use element_str::*;
    pub use element_struct::*;

    mod element;
    mod element_array;
    mod element_bool;
    mod element_f32;
    mod element_f64;
    mod element_i8;
    mod element_i16;
    mod element_i32;
    mod element_i64;
    mod element_str;
    mod element_struct;
}

pub(crate) mod semantic {
    pub(crate) use lexer::*;
    pub(crate) use token_kind::*;

    pub(crate) mod lexer;
    pub(crate) mod token_kind;
}

pub(crate) mod syntactic {
    pub(crate) use parser::*;

    pub(crate) mod parser;
}

pub(crate) mod io;
pub(crate) mod walkable;