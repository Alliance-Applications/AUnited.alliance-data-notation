use crate::data::ElementAccessor;

pub struct ElementI8<'life> {
    name: &'life str,
    value: i8
}

impl ElementAccessor for ElementI8<'_> {

}

impl ElementI8<'_> {

}