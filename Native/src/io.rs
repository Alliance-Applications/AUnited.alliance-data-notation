use std::fs::File;
use std::io::Read;
use std::io::Write;

pub struct NotationFile {
    path: &'static str,
    text: &'a mut String,

}
impl NotationFile {
    pub fn new(path: String) -> NotationFile {
        Self(path)
    }

    pub fn read(&self) -> Result<NotationObject, String> {
        let file = File::open(self.0);

        if file.is_err() {
            return Err(file.err().unwrap().to_string());
        }

        let mut text: String = String::new();
        Read::read_to_string(&mut file.unwrap(), &mut text);

        NotationObject::of(text)
    }

    pub fn write(&self) -> bool {

    }
}