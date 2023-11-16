package fr.inrae.p2m2

case object ReadFile {

  def rsc(path: String): String = {
    import scalajs.js.Dynamic.{global => g}
    val fs = g.require("fs")

    def readFile(name: String): String = {
      fs.readFileSync(name).toString
    }

    readFile(path)
  }
}
