package com.example.footballtickets.activities;

import com.example.footballtickets.R;

public class myData {

    public static String[] matches = {
            "AC Milan vs AS Roma\n29/12/24\n21:45", "Manchester United vs Newcastle\n30/12/24\n22:00", "Maccabi Netanya vs Beitar Jerusalem\n01/01/25\n20:00",
            "Hapoel Beer Sheva vs Maccabi Tel Aviv\n01/01/25\n20:30", "Juventus vs AC Milan\n03/01/25\n21:00", "Liverpool vs Manchester United\n05/01/25\n18:30",
            "Tottenham vs Liverpool\n08/01/25\n22:00", "Borussia Dortmund vs Bayer Leverkusen\n10/01/25\n21:30",
            "Borussia Mönchengladbach vs Bayern Munich\n11/01/25\n19:30", "FC Barcelona vs Valencia\n29/12/25\n21:45"
        };

    public static String[] descriptionArray = {
            "Stadio Giuseppe Meazza", "Old Trafford", "Netanya Stadium", "Yaakov Turner Toto Stadium",
            "Al Awal Park at King Saud University", "Anfield", "Tottenham Hotspur Stadium",
            "Signal Iduna Park Stadium", "Borussia-Park Stadium", "Old Trafford"
    };
    public static int[] prices = {
            150, 100, 150, 150, 200,
            250, 200, 120, 130, 150
    };

    // Store currency symbols separately
    public static String[] currencySymbols = {
            "€", "£", "₪", "₪", "€",
            "£", "£", "€", "€", "€"
    };
    public static Integer[] drawableArrayTeam1 = {
            R.drawable.milan, R.drawable.united, R.drawable.netanya, R.drawable.beersheva, R.drawable.juve,
            R.drawable.liverpool, R.drawable.tothenham, R.drawable.bvb, R.drawable.munich, R.drawable.barca
    };

    public static Integer[] drawableArrayTeam2 = {
            R.drawable.roma, R.drawable.newcastle, R.drawable.beitar, R.drawable.maccabi, R.drawable.milan,
            R.drawable.united, R.drawable.liverpool, R.drawable.lever, R.drawable.gladbach, R.drawable.valencia
    };

    public static Integer[] drawableArrayLeague = {
            R.drawable.seriaa, R.drawable.premier, R.drawable.haal, R.drawable.haal, R.drawable.supercup,
            R.drawable.premier, R.drawable.carabao, R.drawable.bundesliga, R.drawable.bundesliga, R.drawable.laliga

    };

    public static Integer[] id_ = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
}
