# fuck-cors

A Clojure library designed to fuck CORS and open your API completely.
So all cross domain HTTP calls should works.
It should works with cookies too.

In which case should you use this library:

1. You don't have time to think and want something that just works.
2. You don't mind much about security.
3. You hate CORS but want to be able to make Ajax call Cross website.

## Why?

[Some Men Just Want to Watch the World Burn](http://knowyourmeme.com/memes/some-men-just-want-to-watch-the-world-burn)

## Usage

Add

~~~
[fuck-cors 0.1.0]
~~~

to your `project.clj`.

Then

~~~
(:require [fuck-cors.core :refer [wrap-open-cors])
~~~

And use `wrap-open-cors` as middleware.
Now you can make AJAX call from _any_ website.

Of course if you want more control over which website are able to call your API, you should not use this middleware.

## License

Copyright © 2014 Yann Esposito

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
