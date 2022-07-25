use crate::data::ElementAccessor;

pub struct ElementI32<'life> {
    name: &'life str,
    value: i32
}

impl ElementAccessor for ElementI32<'_> {

}

impl ElementI32<'_> {

}