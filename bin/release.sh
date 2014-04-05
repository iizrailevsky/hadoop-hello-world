#!/bin/sh

version=`date -u "+%Y%m%d%H%M%S"`

echo "milestone version: $version"

cp ./build/git/hooks/* .git/hooks

git flow release start -F $version
git flow release finish -m "milestone: $version" -p -D $version