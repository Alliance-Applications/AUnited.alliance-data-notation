use crate::data::ElementAccessor;

pub struct ElementF64<'life> {
    name: &'life str,
    value: f64
}

impl ElementAccessor for ElementF64<'_> {

}

impl ElementF64<'_> {

}