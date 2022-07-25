use std::collections::HashSet;
use crate::data::{Element, ElementAccessor};

pub struct ElementArray<'life> {
    name: &'life str,
    value: HashSet<Element<'life>>
}

impl ElementAccessor for ElementArray<'_> {

}

impl ElementArray<'_> {

}