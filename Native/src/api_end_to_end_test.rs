#[cfg(test)]
mod test {
    use std::collections::HashMap;
    use std::env::temp_dir;

    use crate::data::{Datapoint, Dataset, SearchSet};
    use crate::data::Datapoint::*;
    use crate::data::NotationFormat::{Data, Formatted, Pretty};

    #[test]
    fn given_nothing_then_create_write_read_and_print_dataset() {
        let mut dataset = create_primary_root();
        dataset %= create_secondary_root();

        let mut path = temp_dir();
        path.push("dataset.adn");

        dataset.write_file(&path).unwrap();

        let mut read = Dataset::read_file(path).unwrap();
        compare_dataset(&mut read);
    }

    fn create_primary_root() -> Dataset {
        let mat0 = Array(Box::new(F32(0.0)), vec!(F32(0.5), F32(-1.0)));
        let mat1 = Array(Box::new(F32(0.0)), vec!(F32(0.0), F32(-0.5)));
        let mat = Array(Box::new(F32(0.0)), vec!(mat0, mat1));

        let list = Array(Box::new(I64(0)), vec!(I64(0), I64(1), I64(2), I64(3), I64(4)));

        let mut datastruct: Datapoint = Struct(HashMap::new());

        datastruct.add_to_struct("Matrix".to_string(), mat);
        datastruct.add_to_struct("List".to_string(), list);

        let mut dataset = Dataset::new();
        dataset.add("Primary".to_string(), datastruct);
        dataset
    }

    fn create_secondary_root() -> Dataset {
        let mut dataset = Dataset::new();
        dataset.add("Secondary".to_string(), Struct(HashMap::new()));

        // Test accessor
        dataset.find(&"Secondary".to_string()).unwrap()

            // Test insertion
            .add_to_struct("boolean".to_string(), Bool(true))
            .add_to_struct("byte".to_string(), I8(0))
            .add_to_struct("short".to_string(), I16(0))
            .add_to_struct("int".to_string(), I32(0))
            .add_to_struct("long".to_string(), I64(0))
            .add_to_struct("float".to_string(), F32(0.0))
            .add_to_struct("double".to_string(), F64(0.0))
            .add_to_struct("string".to_string(), Str("0".to_string()))

            // Test removal
            .add_to_struct("useless".to_string(), I32(0))
            .remove_from_struct(&"useless".to_string());

        dataset
    }

    fn compare_dataset(dataset: &mut Dataset) {
        const DAT: &str = "Secondary{boolean true;string \"0\";byte: i8 0;double: f64 0.0;short: i16 0;float 0.0;int 0;long: i64 0;}Primary{Matrix[[0.5,-1.0],[0.0,-0.5]]}";
        assert_eq!(DAT, dataset.set_format(Data).write_string());

        const FMT: &str = "Secondary {\n\
            \tboolean true \n\
            \tstring \"0\" \n\
            \tbyte: i8 0 \n\
            \tdouble: f64 0.0 \n\
            \tshort: i16 0 \n\
            \tfloat 0.0 \n\
            \tint 0 \n\
            \tlong: i64 0 \n\
            }\n\
            \n\
            Primary {\n\
            \tMatrix [\n\
            \t\t[\n\
            \t\t\t0.5, -1.0\n\
            \t\t], [\n\
            \t\t\t0.0, -0.5\n\
            \t\t]\n\
            \t]\n\
            }\n\
            \n";
        assert_eq!(FMT, dataset.set_format(Formatted).write_string());

        const PTY: &str = "struct Secondary {\n\
            \tboolean: bool = true;\n\
            \tstring: str = \"0\";\n\
            \tbyte: i8 = 0;\n\
            \tdouble: f64 = 0.0;\n\
            \tshort: i16 = 0;\n\
            \tfloat: f32 = 0.0;\n\
            \tint: i32 = 0;\n\
            \tlong: i64 = 0;\n\
            }\n\
            \n\
            struct Primary {\n\
            \tMatrix: f32 [\n\
            \t\t[\n\
            \t\t\t0.5, -1.0\n\
            \t\t], [\n\
            \t\t\t0.0, -0.5\n\
            \t\t]\n\
            \t]\n\
            }\n\
            \n";
        assert_eq!(PTY, dataset.set_format(Pretty).write_string());
    }
}