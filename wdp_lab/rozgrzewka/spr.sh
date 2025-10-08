for i in {1..15}
do
    ./roz.exe < testy/dzielny$i.in > wzo.out
    diff -bw wzo.out testy/dzielny$i.out

    if [ $? -eq 0 ]
    then
        echo $i OK
    else
        echo $i BLAD
        break
    fi
done