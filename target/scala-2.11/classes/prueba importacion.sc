val mapa = Map("1"->"37")

mapa("1")


try {
  mapa("1e")
} catch {
  case ex: Exception => {
    println("Missing file exception")
  }
}
