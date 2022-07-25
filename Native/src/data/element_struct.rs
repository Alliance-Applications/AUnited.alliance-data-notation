use std::collections::HashSet;
use crate::data::{Element, ElementAccessor};

pub struct ElementStruct<'life> {
    name: &'life str,
    value: HashSet<Element<'life>>
}

impl ElementAccessor for ElementStruct<'_> {

}

impl ElementStruct<'_> {

}