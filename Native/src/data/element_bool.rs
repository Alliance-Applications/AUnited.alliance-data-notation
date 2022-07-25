use crate::data::ElementAccessor;

pub struct ElementBool<'life> {
    name: &'life str,
    value: bool
}

impl ElementAccessor for ElementBool<'_> {
    fn data_string(&self, input: &mut String) -> String {
        input += format!("bool {}={};")
    }

    fn pretty_string(&self, input: &mut String) -> String {
        todo!()
    }
}

impl ElementBool<'_> {

}