use crate::data::ElementAccessor;

pub struct ElementF32<'life> {
    name: &'life str,
    value: f32
}

impl ElementAccessor for ElementF32<'_> {

}

impl ElementF32<'_> {

}