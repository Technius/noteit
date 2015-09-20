# NoteIt

This is a online notepad webapp, written just for practice. It uses

* Scala
* Play Framework (for the web framework)
* Slick (for database access)
* PureCSS (for styling)

There's no JavaScript at all, since that would take too much time to write.

The webapp is live on Heroku [here](http://boiling-fjord-7572.herokuapp.com/).

TODO:
* Add unit tests
* Logins, just for fun
* Note deleting

## Compiling and Running

Make sure the server has:

* Java 8 or newer
* Access to a Postgresql server

Before running, make sure the database url is configured in `application.conf`.

* Compiling: `activator compile`
* Running (dev mode): `activator run`
* Packaging: `activator universal:packageBin`

## License

NoteIt is licensed under the MIT License. See LICENSE for more details.
