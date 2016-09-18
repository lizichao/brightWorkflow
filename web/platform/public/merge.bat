@echo off
set files=jrafutil.js+jrafajax.js+jrafvtype.js+jrafmodule.js
set tfile=min\jrafajax-p.js

copy %files% /b %tfile%