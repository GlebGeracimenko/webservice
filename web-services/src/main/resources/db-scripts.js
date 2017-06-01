/* remove first character (ususally £) */
db.Item.find({store:"New Look"}).forEach(
  function(e) {
    e.price = e.price.substring(1); // + ".00";
    db.Item.save(e);
  }
)

