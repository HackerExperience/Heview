# HEView Development Guide

So you want to contribute to the Hacker Experience app, huh? Great! We are always
in need of an extra set of hands :)

This quick guide is intended to give you some context about the app, what stuff
you should know about and how to get started.

## About HEView

`HEView` is the front-end client of Hacker Experience 2. It is, basically, a SPA.
That said, we do not use Javascript (directly at least).

## Our Stack

- `HEView` code is written in **Clojurescript**.
- `Clojurescript` is a compiler for the `Clojure` language that yields `Javascript` code.
- The `Clojure` language is a Lisp dialect.
- `Clojurescript` uses the `Google Closure` compiler to get things working.

Furthermore, we also use one framework called `re-frame`.

- `re-frame` is a framework for building reactive SPAs.
- It builds upon several existing frameworks, including `redux` and `Elm`.
- `re-frame`'s presentation layer (views) use `reagent`.
- `reagent` is a Clojurescript wrapper for `React`.
- Therefore, the game components are actually React components.

## What you need to know

In order of importance:

- `re-frame`: You need to know the framework very well. Do take some time to read
its documentation and understand how it works, and how data flows through it.

- `clojurescript`: If you plan to make major changes, you need to know the 
underlying language very well.

- `clojure`: That said, for most cases you do not need to understand the specificities
of `clojure`. Though if you get to know `clojurescript`, you will also get to know `clojure`.

- `javascript`: Again, for most cases you need not know Javascript, unless you plan
to integrate directly with an existing javascript library (say, `Leaflet`).

- `reagent`: If you want to create efficient code, it's a good idea to know how 
reagent works under the hood.

- `react`: Same as above. A minimal understanding of `react` and components'
lifecycle is required in order to create efficient code.

Finally, to understand how `heview` itself works internally, it's essential that
you understand how its state is handled. See docs at `docs/STATE.json`.

## Getting started

### Pre-requisites

- `clojure` (1.10.0).
- `npm` (6.4.x). Older versions probably OK.
- `helix`. [Instructions](https://github.com/hackerexperience/helix).

### Running local dev server

    git clone https://github.com/hackerexperience/heview.git
    cd heview
    npm install
    npm run watch

If all goes well, you should have a working version at `http://localhost:8080`.

### Generating release code

Release (production) code runs several optimizations, and should be used in
any production environment.

    shadow-cljs release prod

If the `prod` versions yields any runtime errors, add a `--debug` flag to the
command above, so you can know exactly which method is crashing.

## Misc

### Application state

The internal application state (`re-frame`'s db) is described at `docs/STATE.json`. Take a look

### Compilation dependency graph

Generates a dependency graph. Useful to make sure your code is decoupled.

    lein nomis-ns-graph :platform cljs :source-paths "src" :exclusions-re "^he\.core"

