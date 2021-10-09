package constants

enum class Header(val type : Char) {
    RELIABLE('a'),
    AUTH('b'),
    VIDEO('c'),
    MSG('d')
}

object Constants {
    const val PORT = 60000
    const val VIDEO = "v"
    const val AUDIO = "a"
    // const val MAX_BUF = 65_535
    const val MAX_BUF = 6_000
    const val SEP = "|" // 구분자
    const val RELIABLE_UID = "-"
    var LONG_TXT = """
        syDXaHU/gnPJ6jXMbv+oFdbVnEozqcVvRDHKjcY
    O3WfuHVObkLNcg+kZa8Q6um3szl1MS13xNobv8R6jXqQ5F0QG/7kFZBk8Qb17krTzp1Fd86YUj08
    wKLYenvJun1Au3XeZ7Sb5mVIC/aqbmclamM6N8yVoe8JVNhvVyeMzXaZaN7h136lFXa4+4635nMy
    5/8Acoy7kBMkqyLjZcgysbmcvEiq8JFypkZZXrxM7qBblTDJdMmwh1W+pMFXVqbYXwyRuarQJQ5Z
    vjUHPvMw5xTvKK6kB5hy2d4PYh1UavUIcsng4jvT/qR1dJ+YbH0yjo5XG9s5Iib1Fy5kVZnWntM5
    Xc5dQ7i5X+IwUvLJ44hll4dTZN8QhbvURqc71KWaRTl+puvpyKnNy87iPfn3BVORZcrqvL1IuY5u
    EX19Ko0Q6ih7yVMtHaD4ko6DE1jpnMf6j1n6kF4PMX5ArUjq1qa9ksaVkuWzkmvKi5N032mcyqmU
    UpkeJnJ7bCc1oZrbN6lFZPXb38TL1gkyle2BohcUfxqI21WpF75j1cy6HjvqYdJJMvtbgO2SiyiZ
    +6zUhyomXdQqsclJXUASPxCurlhF2Jd7my+QGc907iG995FX1mOWpeNWvectbqZyRogdHL+o31IJ
    OZmHM10kDsp25k2pxI6qNx6owVl9y/iI0Fs59SP5j1+YHQeSHVvUi+tX1AauUdDICnmTlnTxcLt9
    xN77d5Bs81AO8ozUNyBB3xxF261KLt0Mzl0k5o1dyuo4kFdfV2jiriVIHiYyRs4gdOqmPVc4mS5j
    27x6+YFqX+JtKMi7LWbj+oReXybiZ7JyM4Cm4HVbm6u1zn1Namvpq4V0c9b5mMpz6rtjeyBR8rk3
    Up+TqnFQPVxOSB0ctky9SniSZHDDblriUdOrYP4k393ohdRu4oodb78QWTfV34mMvMgzk0HMxkow
    txb7TdduuIFDRuLTlvvOS3E3COjkmoOW75Zzcq7/AIYdXiVXXLIr86kiFBxJVxLZPXVJA6Y53THq
    nIyob2TGWrgdHKgmcx1Ici5CPVuB24xv3I6l51Ixyvm+mbq1qRKvq3cbtFnPq/qbFa3DLoZmw5m6
    gpW2cgeq2P8AuGl9TWo3YznETqpuoRbuFGQHeQ5bAmbu4RfXQBzByskXthco6OUHKkkqPeA/buBW
    Wb2jlxzuc20/3DquRp1MuO83X0lPNznaBHKuVhVDbAyp3zI+p/UkbGB0671Bz3vmccstVM5EDo5b
    ubLOycslKqHV1LivEo69VhvZE+R8TkJFzyXRqQfSWleSPX1Hqc3JSoY2ZU8S+MrPk7krrU8TkIaj
    jlXuQVQ99zXV3J2LMp1J3DcNEytm6qXxJUh1W1Ki3PVsFumT1brmY3dsYmqq3mbi5C7q9ELrKEdD
    JqkmdcSHJOOJurREXVdGrZroSR1IsMcrCB0533gXzJcrjtxthT1buBp9SXKqhlfV5INdDJ6vUOrf
    uRitKxumQPVpqHXVXNeKagl78QHqcseJjLc16k5OxqaiMq5W8S+tP3Odq6rZNArJu/UxkAwuyjmS
    csYKctFzXzBzG/UlqyFXZM8anO9sxnXMK6aIXbRJMivMxlUIbLiZVlx2nPlvtM2li1KmrpyL4mvp
    KJLnWia7bYRRkdNx6mr7SB3UxlumBRVtRclOJzRvmJxUBct6lnG+JHBFftKmVinIKh1bK/qT3vtD
    F5v9MsVfVXMOL1ubEeOZlp26kQrx4ivFfuQZQXpYIv8A877TWrcgsN+Y9W9QqrthdNwcoO38RFdO
    ZnIqqnMybam31XCLchkr90i9tSqvH3Cq6unTNdFTm5ZbP9xvW4R0G2Dn91PMDIDniDrK6kV0UGYy
    t1Obe1mxW6hHRy3qHUnMLmct+YVd9Rt3Ayqy5F+Zj7oHRfG6m3UlaCDlTRAsUHyw6+ldbkmSu43z
    AsaxuHW9pzMtuLxVxxaKgdL1cHNq/MlyqF0D2gdMc8u8c2ycut3KMvPiEWNbmM6Nzn1VFyhXTrsP
    E2gt5nMzorzJ62oHXqs1AyqRjl9wHfmYdbeIHXq03xAyZyctTGbV8QOp8jUHnc510t/6mMu/Y5lH
    W5PWlB3k9VsL2sg69eqh1TmZie5uqu8mDoLU3W6kGUTK6lHQzo3Fz3+Jycq7w0t95R0c92cxxy23
    5nPqDf6m6r13kR0c7agvfxIYNNJKLcuIuRx2nLrpqa+8uDr1DqZqpxMm3xN1SK6CuotXV1OWOTds
    eqEVlk5vma1JPX41BythV8muZlU3+5zM/tWTlleh5lxHUyv8Qb/UnqOfEOrt2mUtdHKj9wUtrRJ1
    zdsld3COnXZc31LyL1OQ0a7R6+/eU11EyUk9i5PVaVJc92xg7YvF6uFII95ycrOYmem4wX1cwtk3
    b6myda1ArLKspnIP47/MnpstmEhVGWo471wTm5fcHiGOVkiui0PiSPVluHX1VjJsDmFLlV4zU1J6
    qYuVjKAV5Jgu2Q5PURX7YFj9rfMn/wByRAq4XffiMFdfTKNk5ZMOp7SD65kSXdpzOblqPUhfaa8Y
    Wth5kuSsL1ZxBUuZHQzs1JyW1P5SV/qYPcqm7LZuuh8snJq+0Ba4uVFC2E2WS3vmSbtGSZcXuB0t
    4O8XycyFrIiZCNQKNTLu2T1jIXXsYHV7JJNHEnFUS+GbrvXaUXd77zZJl3kOdcTKY7kD1UOrmxya
    3Jybg7JRfXcMdqshy+3U3VcysdH+Ou0Lu/Mkz0nuDxcFql3cLdl6hdb7QdvM0ijm5V/a8anMyvUM
    uxAs17ihovchdfiHXf6gV2fcw95DlqvMwpQQLcv7hk3/AFDJKXuQxVfUK3UmQVzLJA3zqLlRCm6Y
    uVmqnO2n1F/ipzDKkqTYNQFAvli8MBedTV90MsuiPVRbpYCO3xGx71I3bvU3JUCsc1tmH7k8yelx
    NOojokWHqtrtN1arvIXpfzN1WqRFdHNXfMHLrG5I7/M3UlrAqI1INl6jacyELyTdfaTaNeZrtYF8
    kld12k3lxcw9XqVVru5Jla+Jr1UHLplR0Pt2yV6puqyzvJG8vxIpVO0cWji7hfSwWjUyiscjFexK
    a373OTlQ34mMtWcQOplpPML4kjfeDlYeoNU5XwR6pHVT7ire+IVWW+S2ZyoP+pHX9vMm+oK1A69d
    amM3K/8AUi+qYaQgdXMoe8F6nxI83NwwLdcTddGpO/0SMsldNVA69VVMZfcknJ0ahw7gdHncBRfE
    jrq2OOSu5R07WQ6rkmSD3Jusq5BnK61K69p/ucxNrDN0bsZodMWm4XTqcxQ5muQdTzHJLNzj9SPV
    1FwL6ufE3LfqqkXsmMq7whMk+RxrVXc6BqcXOmOOTZuB0HpZnIPe5z61XxBS5FdXM5hfZ1OY/d6J
    nbzxA69XaK0eZyutkOvqs/uUdlsogZuF6nMzcUvibLK2uZUWZHmvU3VevM5OXSDUeu2FdGgPMc/k
    brnU59UOo15gdOu6P9R6+k4nGqZs0EtkwdXO8Wbrv8zk5UgQMu/eXEdl8Seql7zm/JuiDlZ7gdcE
    NpBzbonPr8cEzlYyjo521qa9VOd97IddbWRl16uAiPM547ubqqRHQb3wQ6ryrtOb8l6ubqDH3LFd
    eqnUOq25xMxY9dX5lHVCxWHUWByzkZ9WLJM3iB36nzc16nH4/k1XaLknEzVdusMZHWpOTmieJRkV
    7hXQyr8yVvj8SDJWbqb5gdDPocUJOStW6nPqcT/3K6/trvBFZO+bgu7k9dyXKvwyxXTqsZBkt95J
    fF6ImVW+ZBePl5qDlXBbI6rvcOu61A6PO49TRXE4qyrZB9Ua1DNE3Jv+mHNDxNMq6lXxESc3jcNI
    VGIvq/3M43+ZK1fmD8mpVXdXAykddljDq6cTV3CLHkm4K7ScslR4iZaqBn7teJhpe0m0ZsXqFqUX
    V7NTOX2h34kGVuodW2QUqZE3VvcnIU51Ju67MDpklEnr+7XEm3EfMMcrxHzArLK/U3Wb3IyvIYWX
    viUdM
    """.replace(" ","")
}

object ClientType {
    const val CASTER = 10 // 방송하는 사람
    const val VIEWER = 20 // 방송을 보는 사람
}