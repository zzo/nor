var FileStuff = JavaImporter(java.io, java.nio.channels, java.nio.charset);

with(FileStuff) {
  exports = {
    exists: function(file) {
      return new File(file).exists();
    }
    , readFileSync: function(file, encoding) {
      var stream, fc, bb;

      encoding = encoding || "UTF-8";

      if (exports.exists(file)) {
        stream = new FileInputStream(new File(file));
        try {
          fc = stream.getChannel();
          bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
          return String(Charset.forName(encoding).decode(bb).toString());
        } finally {
          stream.close();
        }
      }
    }
  };
}
