#!/usr/bin/env bash
set -eu

find src/ -mtime -2s -type f -name "style.clj" -exec touch css.trigger {} +

if [[ -f css.trigger ]]; then
    rm css.trigger
    clojure src/web/style.clj

    echo -e "$(cat assets/css/normalize.css)\n\n\n$(cat target/he.css)" > target/he.css
fi
