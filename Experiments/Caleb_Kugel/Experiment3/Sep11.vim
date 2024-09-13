let SessionLoad = 1
let s:so_save = &g:so | let s:siso_save = &g:siso | setg so=0 siso=0 | setl so=-1 siso=-1
let v:this_session=expand("<sfile>:p")
silent only
silent tabonly
cd ~/Documents/coding/CyWalk/Experiments/Caleb_Kugel/Experiment2
if expand('%') == '' && !&modified && line('$') <= 1 && getline(1) == ''
  let s:wipebuf = bufnr('%')
endif
let s:shortmess_save = &shortmess
if &shortmess =~ 'A'
  set shortmess=aoOA
else
  set shortmess=aoO
endif
badd +1 src/main/java/coms309
badd +124 src/main/java/coms309/people/PeopleController.java
badd +50 src/main/java/coms309/people/Person.java
badd +40 src/main/java/coms309/people/Walking.java
badd +1 src/main/java/coms309/people/Organization.java
argglobal
%argdel
$argadd src/main/java/coms309
edit src/main/java/coms309/people/Organization.java
let s:save_splitbelow = &splitbelow
let s:save_splitright = &splitright
set splitbelow splitright
wincmd _ | wincmd |
vsplit
1wincmd h
wincmd w
let &splitbelow = s:save_splitbelow
let &splitright = s:save_splitright
wincmd t
let s:save_winminheight = &winminheight
let s:save_winminwidth = &winminwidth
set winminheight=0
set winheight=1
set winminwidth=0
set winwidth=1
wincmd =
argglobal
balt src/main/java/coms309/people/Walking.java
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let &fdl = &fdl
let s:l = 35 - ((34 * winheight(0) + 20) / 40)
if s:l < 1 | let s:l = 1 | endif
keepjumps exe s:l
normal! zt
keepjumps 35
normal! 0
lcd ~/Documents/coding/CyWalk/Experiments/Caleb_Kugel/Experiment2
wincmd w
argglobal
if bufexists(fnamemodify("~/Documents/coding/CyWalk/Experiments/Caleb_Kugel/Experiment2/src/main/java/coms309/people/PeopleController.java", ":p")) | buffer ~/Documents/coding/CyWalk/Experiments/Caleb_Kugel/Experiment2/src/main/java/coms309/people/PeopleController.java | else | edit ~/Documents/coding/CyWalk/Experiments/Caleb_Kugel/Experiment2/src/main/java/coms309/people/PeopleController.java | endif
if &buftype ==# 'terminal'
  silent file ~/Documents/coding/CyWalk/Experiments/Caleb_Kugel/Experiment2/src/main/java/coms309/people/PeopleController.java
endif
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let &fdl = &fdl
let s:l = 124 - ((18 * winheight(0) + 20) / 40)
if s:l < 1 | let s:l = 1 | endif
keepjumps exe s:l
normal! zt
keepjumps 124
normal! 04|
lcd ~/Documents/coding/CyWalk/Experiments/Caleb_Kugel/Experiment2
wincmd w
2wincmd w
wincmd =
tabnext 1
if exists('s:wipebuf') && len(win_findbuf(s:wipebuf)) == 0 && getbufvar(s:wipebuf, '&buftype') isnot# 'terminal'
  silent exe 'bwipe ' . s:wipebuf
endif
unlet! s:wipebuf
set winheight=1 winwidth=20
let &shortmess = s:shortmess_save
let &winminheight = s:save_winminheight
let &winminwidth = s:save_winminwidth
let s:sx = expand("<sfile>:p:r")."x.vim"
if filereadable(s:sx)
  exe "source " . fnameescape(s:sx)
endif
let &g:so = s:so_save | let &g:siso = s:siso_save
set hlsearch
nohlsearch
doautoall SessionLoadPost
unlet SessionLoad
" vim: set ft=vim :
