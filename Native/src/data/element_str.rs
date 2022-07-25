use crate::data::ElementAccessor;

pub struct ElementStr<'life> {
    name: &'life str,
    value: &'life str
}

impl ElementAccessor for ElementStr<'_> {

}

impl ElementStr<'_> {

}