use crate::data::ElementAccessor;

pub struct ElementI16<'life> {
    name: &'life str,
    value: i16
}

impl ElementAccessor for ElementI16<'_> {

}

impl ElementI16<'_> {

}