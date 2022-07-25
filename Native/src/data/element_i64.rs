use crate::data::ElementAccessor;

pub struct ElementI64<'life> {
    name: &'life str,
    value: i64
}

impl ElementAccessor for ElementI64<'_> {

}

impl ElementI64<'_> {

}