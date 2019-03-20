package com.baltimore.model;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baltimore.common.Configuration.FUZZY_SEARCH_MINIMUM_CONFIDENCE_SCORE;
import static com.baltimore.common.Configuration.FUZZY_SEARCH_QUICK;

/**
 * Created by paul on 14.09.18.
 */
public class Neighborhood {

    final private static Map<String, PoliceDistrict> policeDistrictLookup;
    final private static Map<String, String> cityCouncilLookup;

    final private static String unknown = "unknown";
    private static final List<String> central;
    private static final List<String> southeastern;
    private static final List<String> eastern;
    private static final List<String> northeastern;
    private static final List<String> northern;
    private static final List<String> northwestern;
    private static final List<String> western;
    private static final List<String> southwestern;
    private static final List<String> southern;
    private static final List<String> police_neighborhoods;
    private static final List<String> city_council_neighborhoods;
    private static final List<String> council1;
    private static final List<String> council2;
    private static final List<String> council3;
    private static final List<String> council4;
    private static final List<String> council5;
    private static final List<String> council6;
    private static final List<String> council7;
    private static final List<String> council8;
    private static final List<String> council9;
    private static final List<String> council10;
    private static final List<String> council11;
    private static final List<String> council12;
    private static final List<String> council13;
    private static final List<String> council14;

    static {

        police_neighborhoods = new ArrayList<>();
        city_council_neighborhoods = new ArrayList<>();
        cityCouncilLookup = new HashMap<>();
        policeDistrictLookup = new HashMap<>();
        central = Arrays.asList(new String[]{"Barclay", "Bolton Hill", "Charles North", "Downtown", "Downtown West", "Druid Heights", "Greenmount West", "Heritage Crossing", "Inner Harbor", "Madison Park", "Mid-Town Belvedere", "Mount Vernon", "Poppleton", "Reservoir Hill", "Seton Hill", "University Of Maryland", "Upton"});
        southeastern = Arrays.asList(new String[]{"Baltimore Highlands", "Bayview", "Brewers Hill", "Broening Manor", "Butcher\'s Hill", "Canton", "Canton Industrial Area", "CARE", "Dunbar-Broadway", "Dundalk Marine Terminal", "Eastwood", "Ellwood Park/Monument", "Fells Point", "Graceland Park", "Greektown", "Highlandtown", "Holabird Industrial Park", "Hopkins Bayview", "Jonestown", "Kresson", "Little Italy", "McElderry Park", "Medford", "O\'Donnell Heights", "Patterson Park", "Patterson Park Neighborhood", "Patterson Place", "Perkins Homes", "Pleasant View Gardens", "Pulaski Industrial Area", "Saint Helena", "Upper Fells Point", "Washington Hill"});
        eastern = Arrays.asList(new String[]{"Barclay", "Berea", "Biddle Street", "Broadway East", "CARE", "Darley Park", "Dunbar-Broadway", "East Baltimore Midway", "Gay Street", "Greenmount Cemetery", "Greenmount West", "Inner Harbor", "Johnston Square", "Madison-Eastend", "Middle East", "Milton-Montford", "Oldtown", "Oliver", "Orangeville", "Orangeville Industrial Areav", "Penn-Fallsway", "South Clifton Park"});
        northeastern = Arrays.asList(new String[]{"Arcadia", "Armistead Gardens", "Belair-Edison", "Belair-Parkside", "Better Waverly", "Beverly Hills", "Cedmont", "Cedonia", "Clifton Park", "Coldstream Homestead Montebello", "Darley Park", "Ednor Gardens-Lakeside", "Four By Four", "Frankford", "Glenham-Belhar", "Hamilton Hills", "Herring Run Park", "Hillen", "Idlewood", "Lauraville", "Loch Raven", "Lower Herring Run Park", "Mayfield", "Montebello", "Moravia-Walther", "Morgan Park", "Morgan State University", "Mt Pleasant Park", "New Northwood", "North Harford Road", "Orchard Ridge", "Original Northwood", "Overlea", "Parkside", "Perring Loch", "Pulaski Industrial Area", "Ramblewood", "Rosemont East", "Stonewood-Pentwood-Winston", "Taylor Heights", "Waltherson", "Westfield", "Woodbourne Heights", "Claremont - Freedom", "Glenham-Belford"});
        northern = Arrays.asList(new String[]{"Abell", "Barclay", "Bellona-Gittings", "Belvedere", "Better Waverly", "Blythewood", "Cameron Village", "Cedarcroft", "Charles North", "Charles Village", "Chinquapin Park", "Coldspring", "Cross Keys", "Cylburn", "Druid Hill Park", "Evergreen", "Evesham Park", "Glen Oaks", "Greenspring", "Guilford", "Hampden", "Harwood", "Hoes Heights", "Homeland", "Johns Hopkins Homewood", "Jones Falls Area", "Kenilworth Park", "Kernewood", "Keswick", "Lake Evesham", "Lake Walker", "Levindale", "Loyola/Notre Dame", "Medfield", "Mid-Govans", "Mount Washington", "North Roland Park/Poplar Hill", "Oakenshawe", "Old Goucher", "Parklane", "Pen Lucy", "Radnor-Winston", "Remington", "Richnor Springs", "Roland Park", "Rosebank", "Sabina-Mattfeldt", "The Orchards", "Tuscany-Canterbury", "Villages Of Homeland", "Waverly", "Wilson Park", "Winston-Govans", "Woodberry", "Woodbourne-McCabe", "Wrenlane", "Wyman Park", "Wyndhurst", "York-Homeland"});
        northwestern = Arrays.asList(new String[]{"Arlington", "Ashburton", "Burleith-Leighton", "Callaway-Garrison", "Central Forest Park", "Central Park Heights", "Cheswolde", "Concerned Citizens Of Forest Park", "Cross Country", "Dolfield", "Dorchester", "East Arlington", "Fallstaff", "Forest Park", "Forest Park Golf Course", "Garwyn Oaks", "Glen", "Grove Park", "Hanlon-Longwood", "Howard Park", "Langston Hughes", "Liberty Square", "Lucille Park", "Park Circle", "Pimlico Good Neighbors", "Purnell", "Reisterstown Station", "Seton Business Park", "Towanda-Grantley", "West Arlington", "West Forest Park", "Windsor Hills", "Woodmere"});
        western = Arrays.asList(new String[]{"Bridgeview/Greenlawn", "Coppin Heights/Ash-Co-East", "Easterwood", "Evergreen Lawn", "Franklin Square", "Harlem Park", "Midtown-Edmondson", "Mondawmin", "Mosher", "Panway/Braddish Avenue", "Parkview/Woodbrook", "Penn North", "Penrose/Fayette Street Outreach", "Poppleton", "Rosemont Homeowners/Tenants", "Sandtown-Winchester"});
        southwestern = Arrays.asList(new String[]{"Allendale", "Beechfield", "Boyd-Booth", "Carroll-South Hilton", "Carrollton Ridge", "Dickeyville", "Edgewood", "Edmondson Village", "Fairmont", "Franklintown", "Franklintown Road", "Gwynns Falls", "Gwynns Falls/Leakin Park", "Hunting Ridge", "Irvington", "Lower Edmondson Village", "Millhill", "Morrell Park", "Mount Holly", "Northwest Community Action", "Oaklee", "Penrose/Fayette Street Outreach", "Rognel Heights", "Rosemont", "Saint Agnes", "Saint Josephs", "Shipley Hill", "Ten Hills", "Tremont", "Uplands", "Violetville", "Wakefield", "Walbrook", "West Hills", "Westgate", "Wilhelm Park", "Winchester", "Yale Heights"});
        southern = Arrays.asList(new String[]{"Barre Circle", "Brooklyn", "Carroll - Camden Industrial Area", "Carroll Park", "Cherry Hill", "Curtis Bay", "Curtis Bay Industrial Area", "Fairfield Area", "Federal Hill", "Hawkins Point", "Hollins Market", "Lakeland", "Locust Point", "Locust Point Industrial Area", "Middle Branch/Reedbird Parks", "New Southwest/Mount Clare", "Mount Winans", "Otterbein", "Port Covington", "Ridgely's Delight", "Riverside", "Saint Paul", "South Baltimore", "Sharp-Leadenhall", "Spring Garden Industrial Area", "Stadium Area", "Union Square", "Washington Village/Pigtown", "Westport", "Carrollton Ridge", "Downtown West", "Inner Harbor", "Franklin Square"});

        council1 = Arrays.asList(new String[]{"baltimore highlands", "bayview", "brewers hill", "broening manor", "butcher's hill", "canton", "canton industrial area", "eastwood", "ellwood park/monument", "fells point", "graceland park", "greektown", "highlandtown", "holabird industrial park", "hopkins bayview", "inner harbor", "little italy", "medford", "o'donnell heights", "patterson park", "patterson park neighborhood", "patterson place", "pulaski industrial area", "saint helena", "upper fells point"});
        council2 = Arrays.asList(new String[]{"armistead gardens", "baltimore highlands", "belair-edison", "cedmont", "cedonia", "frankford", "glenham-belhar", "highlandtown", "kresson", "north harford road", "orangeville", "overlea", "parkside", "pulaski industrial area", "rosemont east", "taylor heights", "waltherson", "westfield"});
        council3 = Arrays.asList(new String[]{"arcadia", "belair-edison", "belair-parkside", "beverly hills", "glenham-belhar", "hamilton hills", "hillen", "lauraville", "loch raven", "mayfield", "moravia-walther", "morgan park", "morgan state university", "new northwood", "north harford road", "original northwood", "perring loch", "stonewood-pentwood-winston", "taylor heights", "waltherson", "westfield"});
        council4 = Arrays.asList(new String[]{"bellona-gittings", "belvedere", "cameron village", "cedarcroft", "chinquapin park", "evesham park", "glen oaks", "guilford", "homeland", "idlewood", "kenilworth park", "kernewood", "lake evesham", "lake walker", "loch raven", "loyola/notre dame", "mid-govans", "new northwood", "pen lucy", "radnor-winston", "ramblewood", "richnor springs", "rosebank", "waverly", "wilson park", "winston-govans", "woodbourne heights", "woodbourne-mccabe", "wrenlane", "york-homeland"});
        council5 = Arrays.asList(new String[]{"arlington", "cheswolde", "cross country", "dorchester", "fallstaff", "glen", "grove park", "howard park", "mount washington", "north roland park/poplar hill", "pimlico good neighbors", "reisterstown station", "sabina-mattfeldt", "seton business park", "west arlington", "woodmere"});
        council6 = Arrays.asList(new String[]{"ashburton", "blythewood", "callaway-garrison", "central forest park", "central park heights", "coldspring", "concerned citizens of forest park", "cross keys", "cylburn", "dolfield", "dorchester", "east arlington", "forest park", "garwyn oaks", "greenspring", "langston hughes", "levindale", "loyola/notre dame", "lucille park", "mount holly", "park circle", "parklane", "roland park", "towanda-grantley", "west arlington", "west forest park", "windsor hills", "woodmere", "wyndhurst"});
        council7 = Arrays.asList(new String[]{"ashburton", "bridgeview/greenlawn", "burleith-leighton", "coppin heights/ash-co-east", "druid heights", "east arlington", "easterwood", "fairmont", "forest park", "hampden", "hanlon-longwood", "hoes heights", "jones falls area", "liberty square", "medfield", "mondawmin", "mount holly", "northwest community action", "panway/braddish avenue", "park circle", "parkview/woodbrook", "penn north", "reservoir hill", "roland park", "rosemont", "sandtown-winchester", "walbrook", "woodberry"});
        council8 = Arrays.asList(new String[]{"allendale", "beechfield", "central forest park", "edgewood", "edmondson village", "forest park golf course", "franklintown", "howard park", "hunting ridge", "irvington", "lower edmondson village", "rognel heights", "saint agnes", "saint josephs", "ten hills", "tremont", "uplands", "wakefield", "west forest park", "west hills", "westgate", "windsor hills", "yale heights"});
        council9 = Arrays.asList(new String[]{"barre circle", "boyd-booth", "bridgeview/greenlawn", "carroll-south hilton", "carrollton ridge", "coppin heights/ash-co-east", "evergreen lawn", "franklin square", "franklintown road", "harlem park", "hollins market", "midtown-edmondson", "millhill", "mosher", "new southwest/mount clare", "northwest community action", "penrose/fayette street outreach", "poppleton", "rosemont", "rosemont homeowners/tenants", "sandtown-winchester", "shipley hill", "union square", "winchester"});
        council10 = Arrays.asList(new String[]{"barre circle", "brooklyn", "carroll - camden industrial area", "carroll park", "cherry hill", "curtis bay", "curtis bay industrial area", "fairfield area", "gwynns falls", "hawkins point", "lakeland", "middle branch/reedbird parks", "millhill", "morrell park", "mount winans", "oaklee", "saint agnes", "saint paul", "violetville", "washington village/pigtown", "westport", "wilhelm park"});
        council11 = Arrays.asList(new String[]{"bolton hill", "carroll - camden industrial area", "downtown", "downtown west", "druid heights", "federal hill", "heritage crossing", "inner harbor", "locust point", "locust point industrial area", "madison park", "mid-town belvedere", "mount vernon", "otterbein", "penn-fallsway", "poppleton", "port covington", "ridgely's delight", "riverside", "seton hill", "sharp-leadenhall", "south baltimore", "spring garden industrial area", "university of maryland", "upton"});
        council12 = Arrays.asList(new String[]{"abell", "barclay", "broadway east", "charles north", "charles village", "darley park", "dunbar-broadway", "east baltimore midway", "fells point", "gay street", "greenmount west", "harwood", "johnston square", "jones falls area", "jonestown", "little italy", "mid-town belvedere", "middle east", "mount vernon", "old goucher", "oldtown", "oliver", "penn-fallsway", "perkins homes", "pleasant view gardens", "remington", "south clifton park", "washington hill"});
        council13 = Arrays.asList(new String[]{"armistead gardens", "belair-edison", "berea", "biddle street", "broadway east", "butcher's hill", "care", "dunbar-broadway", "ellwood park/monument", "four by four", "madison-eastend", "mcelderry park", "middle east", "milton-montford", "orangeville", "orangeville industrial area", "orchard ridge", "patterson place", "upper fells point", "washington hill"});
        council14 = Arrays.asList(new String[]{"abell", "better waverly", "charles village", "clifton park", "coldstream homestead montebello", "darley park", "ednor gardens-lakeside", "guilford", "hampden", "hillen", "hoes heights", "johns hopkins homewood", "keswick", "mayfield", "oakenshawe", "original northwood", "remington", "roland park", "tuscany-canterbury", "waverly", "wyman park"});

        initMap(central, PoliceDistrict.Central);
        initMap(southeastern, PoliceDistrict.Southeastern);
        initMap(eastern, PoliceDistrict.Eastern);
        initMap(northeastern, PoliceDistrict.Northeastern);
        initMap(northern, PoliceDistrict.Northern);
        initMap(northwestern, PoliceDistrict.Northwestern);
        initMap(western, PoliceDistrict.Western);
        initMap(southwestern, PoliceDistrict.Southwestern);
        initMap(southern, PoliceDistrict.Southern);

        initMap("1", council1);
        initMap("2", council2);
        initMap("3", council3);
        initMap("4", council4);
        initMap("5", council5);
        initMap("6", council6);
        initMap("7", council7);
        initMap("8", council8);
        initMap("9", council9);
        initMap("10", council10);
        initMap("11", council11);
        initMap("12", council12);
        initMap("13", council13);
        initMap("14", council14);


        police_neighborhoods.addAll(central);
        police_neighborhoods.addAll(southern);
        police_neighborhoods.addAll(eastern);
        police_neighborhoods.addAll(northeastern);
        police_neighborhoods.addAll(northern);
        police_neighborhoods.addAll(northwestern);
        police_neighborhoods.addAll(western);
        police_neighborhoods.addAll(southwestern);
        police_neighborhoods.addAll(southern);

        city_council_neighborhoods.addAll(council1);
        city_council_neighborhoods.addAll(council2);
        city_council_neighborhoods.addAll(council3);
        city_council_neighborhoods.addAll(council4);
        city_council_neighborhoods.addAll(council5);
        city_council_neighborhoods.addAll(council6);
        city_council_neighborhoods.addAll(council7);
        city_council_neighborhoods.addAll(council8);
        city_council_neighborhoods.addAll(council9);
        city_council_neighborhoods.addAll(council10);
        city_council_neighborhoods.addAll(council11);
        city_council_neighborhoods.addAll(council12);
        city_council_neighborhoods.addAll(council13);
        city_council_neighborhoods.addAll(council14);

    }

    private static void initMap(Collection<String> collection, PoliceDistrict district) {
        for (String n : collection) {
            policeDistrictLookup.put(n, district);
        }
    }

    private static void initMap(String value, Collection<String> keys) {
        for (String key : keys) {
            cityCouncilLookup.put(key, value);
        }
    }

    public static PoliceDistrict policeDistrictOf(String neighborhood) {
        try {
            PoliceDistrict district = policeDistrictLookup.getOrDefault(neighborhood.trim(), PoliceDistrict.unknown);

            if (district == PoliceDistrict.unknown) {
                return fuzzyMapPoliceDistrictTo(neighborhood);
            } else {
                return district;
            }

        } catch (NullPointerException e) {
            return PoliceDistrict.unknown;
        }

    }

    public static String cityCouncilOf(String neighborhood) {
        String tmp = neighborhood.toLowerCase();
        String council = cityCouncilLookup.getOrDefault(tmp, unknown);
        try {
            if (unknown.equalsIgnoreCase(council)) {
                return fuzzyMapNeighborhoodToCouncil(tmp);
            } else {
                return council;
            }
        } catch (Exception e) {
            return unknown;
        }
    }

    private static PoliceDistrict fuzzyMapPoliceDistrictTo(String neighborhood) {
        ExtractedResult result = findNearestMatchInCollection(neighborhood, police_neighborhoods);
        return doesResultMeetThreshold(result) ? policeDistrictOf(result.getString()) : PoliceDistrict.unknown;
    }

    private static String fuzzyMapNeighborhoodToCouncil(String neighborhood) {
        ExtractedResult result = findNearestMatchInCollection(neighborhood, city_council_neighborhoods);
        return doesResultMeetThreshold(result) ? cityCouncilOf(result.getString()) : unknown;
    }

    private static ExtractedResult findNearestMatchInCollection(String text, Collection collection) {
        if (FUZZY_SEARCH_QUICK) {
            return FuzzySearch.extractOne(text, collection);
        } else {
            List<ExtractedResult> best = FuzzySearch.extractTop(text, collection, 3);
            int bestResult = 0;
            for (int i = 0; i < best.size(); i++) {
                if (best.get(i).getScore() >= best.get(bestResult).getScore()) {
                    bestResult = i;
                }
            }
            return best.get(bestResult);
        }
    }

    private static boolean doesResultMeetThreshold(ExtractedResult result) {
        return result.getScore() > FUZZY_SEARCH_MINIMUM_CONFIDENCE_SCORE;
    }

}

