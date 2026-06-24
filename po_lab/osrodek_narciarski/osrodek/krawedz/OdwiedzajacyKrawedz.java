package osrodek_narciarski.osrodek.krawedz;

import osrodek_narciarski.osrodek.krawedz.wyciag.Wyciag;

public interface OdwiedzajacyKrawedz<R> {
    public R trasa(Trasa trasa);
    public R wyciag(Wyciag wyciag);
}
