parallelExecution in Global := false

lazy val DifferentiableKernel =
  project.dependsOn(
    OpenCL,
    OpenCLCodeGenerator,
    Compute
  )

lazy val OpenCLCodeGenerator = project.dependsOn(Memory)

//lazy val Layer = project.dependsOn(ProjectRef(file("RAII.scala"), "ResourceFactoryTJVM"))

//lazy val CumulativeTape = project.dependsOn(Layer, ProjectRef(file("RAII.scala"), "Shared"))

//lazy val CheckedTape = project.dependsOn(Layer, Closeables)

// TODO: Move to a separate repository
lazy val Memory = project

lazy val Tape = project

lazy val Compute = project.dependsOn(
  Tape,
  ProjectRef(file("RAII.scala"), "EitherTNondeterminismJVM"),
  ProjectRef(file("RAII.scala"), "Shared"),
  ProjectRef(file("RAII.scala"), "ResourceFactoryTJVM")
)

lazy val Closeables = project

//// TODO: Rename to ToLiteral?
//lazy val Symbolic = project.dependsOn(Layer)

includeFilter in unmanagedSources := (includeFilter in unmanagedSources).value && new SimpleFileFilter(_.isFile)

lazy val OpenCL = project.dependsOn(Closeables, Memory, ProjectRef(file("RAII.scala"), "ResourceFactoryTJVM"))

//lazy val LayerFactory = project.dependsOn(DifferentiableKernel)

lazy val Float = project.dependsOn(Compute)

//lazy val DifferentiableDouble =
//  project.dependsOn(Layer,
//                    CumulativeTape,
//                    CheckedTape,
//                    ProjectRef(file("Future.scala"), "concurrent-Converters") % Test,
//                    Symbolic % Test)
//
//lazy val DifferentiableInt =
//  project.dependsOn(Layer,
//                    CumulativeTape,
//                    CheckedTape,
//                    Float,
//                    ProjectRef(file("Future.scala"), "sde-task") % Test,
//                    Symbolic % Test)
//
//val FloatRegex = """(?i:float)""".r
//
//sourceGenerators in Compile in DifferentiableDouble += Def.task {
//  for {
//    floatFile <- (unmanagedSources in Compile in Float).value
//    relativeFile <- floatFile.relativeTo((sourceDirectory in Compile in Float).value)
//  } yield {
//    val floatSource = IO.read(floatFile, scala.io.Codec.UTF8.charSet)
//
//    val doubleSource = FloatRegex.replaceAllIn(floatSource, { m =>
//      m.matched match {
//        case "Float" => "Double"
//        case "float" => "double"
//      }
//    })
//
//    val outputFile = (sourceManaged in Compile in DifferentiableDouble).value / relativeFile.getPath
//    IO.write(outputFile, doubleSource, scala.io.Codec.UTF8.charSet)
//    outputFile
//  }
//}.taskValue

dependsOn(Float)

enablePlugins(TravisUnidocTitle)

libraryDependencies += "org.scalaz" %% "scalaz-concurrent" % "7.2.10"

UnidocKeys.unidocProjectFilter in ScalaUnidoc in UnidocKeys.unidoc := inAggregates(LocalRootProject)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

scalaOrganization in updateSbtClassifiers := (scalaOrganization in Global).value

scalaOrganization := "org.typelevel"

scalacOptions += "-Yliteral-types"

name := "deeplearning"

organization in ThisBuild := "com.thoughtworks.deeplearning"

crossScalaVersions := Seq("2.11.8", "2.12.1")

fork in ThisBuild in Test := true
