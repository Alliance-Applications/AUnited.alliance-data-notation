use crate::data::{ElementArray, ElementBool, ElementI16, ElementI32, ElementI64, ElementI8, ElementStr, ElementStruct};

pub enum Element<'life> {
    ElementArray(ElementArray<'life>),
    ElementBool(ElementBool<'life>),
    ElementF32(ElementBool<'life>),
    ElementF64(ElementBool<'life>),
    ElementI8(ElementI8<'life>),
    ElementI16(ElementI16<'life>),
    ElementI32(ElementI32<'life>),
    ElementI64(ElementI64<'life>),
    ElementStr(ElementStr<'life>),
    ElementStruct(ElementStruct<'life>),
}

pub trait ElementAccessor {

    fn data_string(&self, input: &mut String) -> String;

    fn pretty_string(&self, input: &mut String) -> String;

}