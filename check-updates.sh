#!/usr/bin/env bash
./mvnw versions:display-parent-updates \
  versions:display-dependency-updates \
  versions:display-plugin-updates \
  -DprocessDependencyManagement=false
