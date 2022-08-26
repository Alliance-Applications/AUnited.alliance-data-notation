pub(crate) trait Walkable<T> {
    fn size(&self) -> usize;
    fn peek(&self, offset: usize) -> T;
    fn skip(&mut self);

    #[inline]
    fn current(&self) -> T {
        self.peek(0)
    }

    #[inline]
    fn next(&self) -> T {
        self.peek(1)
    }

    fn consume(&mut self) -> T {
        let token = self.current();
        self.skip();
        token
    }
}