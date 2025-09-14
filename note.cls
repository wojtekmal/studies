% note.cls
\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{note}[2025/09/14 Custom Math + CS Notes Class]

% Base class
\LoadClass[12pt]{article}

% Packages
\RequirePackage[utf8]{inputenc}
\RequirePackage[T1]{fontenc}
\RequirePackage{lmodern}
\RequirePackage{amsmath, amssymb, amsthm, mathtools}
\RequirePackage{geometry}
\RequirePackage{xcolor}
\RequirePackage{hyperref}
\RequirePackage{graphicx}
\RequirePackage{listings}
\RequirePackage{environ}

% Page layout
\geometry{a4paper, margin=1in}

% Hyperlinks
\hypersetup{
    colorlinks=true,
    linkcolor=blue,
    urlcolor=blue,
    citecolor=blue
}

% Safer custom commands for math symbols
\providecommand{\R}{\mathbb{R}}
\providecommand{\N}{\mathbb{N}}
\providecommand{\Z}{\mathbb{Z}}
\providecommand{\C}{\mathbb{C}}

% Listings for code (CS)
\lstset{
    basicstyle=\ttfamily\small,
    keywordstyle=\color{blue},
    commentstyle=\color{green!50!black},
    stringstyle=\color{red},
    numbers=left,
    numberstyle=\tiny\color{gray},
    stepnumber=1,
    numbersep=5pt,
    frame=tb,
    breaklines=true,
    showstringspaces=false,
    tabsize=4
}

% --- Custom Environments ---

% An exercise environment with a mandatory argument for custom numbering
\NewEnviron{exercise}[1]{
    \par\medskip
    \noindent\textbf{Exercise #1.}\\ % Print the custom number
    \BODY
    \medskip
}

% A solution environment that uses the stored exercise number
\NewEnviron{solution}{
    \par\medskip
    \noindent\textbf{Solution}\\
    \BODY
    \medskip
}