package com.baltimore.common.data;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.baltimore.common.Configuration.FUZZY_SEARCH_MINIMUM_CONFIDENCE_SCORE;

/**
 * Created by paul on 14.09.18.
 */
public class Neighborhood {

    final private static Map<String, PoliceDistrict> map;

    private static final List<String> central;
    private static final List<String> southeastern;
    private static final List<String> eastern;
    private static final List<String> northeastern;
    private static final List<String> northern;
    private static final List<String> northwestern;
    private static final List<String> western;
    private static final List<String> southwestern;
    private static final List<String> southern;
    private static final List<String> all;

    static {

        all = new ArrayList<>();
        map = new HashMap<>();
        central = Arrays.asList(new String[]{"Barclay","Bolton Hill","Charles North","Downtown","Downtown West","Druid Heights","Greenmount West","Heritage Crossing","Inner Harbor","Madison Park","Mid-Town Belvedere","Mount Vernon","Poppleton","Reservoir Hill","Seton Hill","University Of Maryland","Upton"});
        southeastern = Arrays.asList(new String[]{"Baltimore Highlands","Bayview","Brewers Hill","Broening Manor","Butcher\'s Hill","Canton","Canton Industrial Area","CARE","Dunbar-Broadway","Dundalk Marine Terminal","Eastwood","Ellwood Park/Monument","Fells Point","Graceland Park","Greektown","Highlandtown","Holabird Industrial Park","Hopkins Bayview","Jonestown","Kresson","Little Italy","McElderry Park","Medford","O\'Donnell Heights","Patterson Park","Patterson Park Neighborhood","Patterson Place","Perkins Homes","Pleasant View Gardens","Pulaski Industrial Area","Saint Helena","Upper Fells Point","Washington Hill"});
        eastern = Arrays.asList(new String[]{"Barclay","Berea","Biddle Street","Broadway East","CARE","Darley Park","Dunbar-Broadway","East Baltimore Midway","Gay Street","Greenmount Cemetery","Greenmount West","Inner Harbor","Johnston Square","Madison-Eastend","Middle East","Milton-Montford","Oldtown","Oliver","Orangeville","Orangeville Industrial Areav","Penn-Fallsway","South Clifton Park"});
        northeastern = Arrays.asList(new String[]{"Arcadia","Armistead Gardens","Belair-Edison","Belair-Parkside","Better Waverly","Beverly Hills","Cedmont","Cedonia","Clifton Park","Coldstream Homestead Montebello","Darley Park","Ednor Gardens-Lakeside","Four By Four","Frankford","Glenham-Belhar","Hamilton Hills","Herring Run Park","Hillen","Idlewood","Lauraville","Loch Raven","Lower Herring Run Park","Mayfield","Montebello","Moravia-Walther","Morgan Park","Morgan State University","Mt Pleasant Park","New Northwood","North Harford Road","Orchard Ridge","Original Northwood","Overlea","Parkside","Perring Loch","Pulaski Industrial Area","Ramblewood","Rosemont East","Stonewood-Pentwood-Winston","Taylor Heights","Waltherson","Westfield","Woodbourne Heights"});
        northern = Arrays.asList(new String[]{"Abell","Barclay","Bellona-Gittings","Belvedere","Better Waverly","Blythewood","Cameron Village","Cedarcroft","Charles North","Charles Village","Chinquapin Park","Coldspring","Cross Keys","Cylburn","Druid Hill Park","Evergreen","Evesham Park","Glen Oaks","Greenspring","Guilford","Hampden","Harwood","Hoes Heights","Homeland","Johns Hopkins Homewood","Jones Falls Area","Kenilworth Park","Kernewood","Keswick","Lake Evesham","Lake Walker","Levindale","Loyola/Notre Dame","Medfield","Mid-Govans","Mount Washington","North Roland Park/Poplar Hill","Oakenshawe","Old Goucher","Parklane","Pen Lucy","Radnor-Winston","Remington","Richnor Springs","Roland Park","Rosebank","Sabina-Mattfeldt","The Orchards","Tuscany-Canterbury","Villages Of Homeland","Waverly","Wilson Park","Winston-Govans","Woodberry","Woodbourne-McCabe","Wrenlane","Wyman Park","Wyndhurst","York-Homeland"});
        northwestern = Arrays.asList(new String[]{"Arlington","Ashburton","Burleith-Leighton","Callaway-Garrison","Central Forest Park","Central Park Heights","Cheswolde","Concerned Citizens Of Forest Park","Cross Country","Dolfield","Dorchester","East Arlington","Fallstaff","Forest Park","Forest Park Golf Course","Garwyn Oaks","Glen","Grove Park","Hanlon-Longwood","Howard Park","Langston Hughes","Liberty Square","Lucille Park","Park Circle","Pimlico Good Neighbors","Purnell","Reisterstown Station","Seton Business Park","Towanda-Grantley","West Arlington","West Forest Park","Windsor Hills","Woodmere"});
        western = Arrays.asList(new String[]{"Bridgeview/Greenlawn","Coppin Heights/Ash-Co-East","Easterwood","Evergreen Lawn","Franklin Square","Harlem Park","Midtown-Edmondson","Mondawmin","Mosher","Panway/Braddish Avenue","Parkview/Woodbrook","Penn North","Penrose/Fayette Street Outreach","Poppleton","Rosemont Homeowners/Tenants","Sandtown-Winchester"});
        southwestern = Arrays.asList(new String[]{"Allendale","Beechfield","Boyd-Booth","Carroll-South Hilton","Carrollton Ridge","Dickeyville","Edgewood","Edmondson Village","Fairmont","Franklintown","Franklintown Road","Gwynns Falls","Gwynns Falls/Leakin Park","Hunting Ridge","Irvington","Lower Edmondson Village","Millhill","Morrell Park","Mount Holly","Northwest Community Action","Oaklee","Penrose/Fayette Street Outreach","Rognel Heights","Rosemont","Saint Agnes","Saint Josephs","Shipley Hill","Ten Hills","Tremont","Uplands","Violetville","Wakefield","Walbrook","West Hills","Westgate","Wilhelm Park","Winchester","Yale Heights"});
        southern = Arrays.asList(new String[]{"Barre Circle","Brooklyn","Carroll - Camden Industrial Area","Carroll Park","Cherry Hill","Curtis Bay","Curtis Bay Industrial Area","Fairfield Area","Federal Hill","Hawkins Point","Hollins Market","Lakeland","Locust Point","Locust Point Industrial Area","Middle Branch/Reedbird Parks","New Southwest/Mount Clare","Mount Winans","Otterbein","Port Covington","Ridgely's Delight","Riverside","Saint Paul","South Baltimore","Sharp-Leadenhall","Spring Garden Industrial Area","Stadium Area","Union Square","Washington Village/Pigtown","Westport","Carrollton Ridge","Downtown West","Inner Harbor","Franklin Square"});

        initMap(central, PoliceDistrict.Central);
        initMap(southeastern, PoliceDistrict.Southeastern);
        initMap(eastern, PoliceDistrict.Eastern);
        initMap(northeastern, PoliceDistrict.Northeastern);
        initMap(northern, PoliceDistrict.Northern);
        initMap(northwestern, PoliceDistrict.Northwestern);
        initMap(western, PoliceDistrict.Western);
        initMap(southwestern, PoliceDistrict.Southwestern);
        initMap(southern, PoliceDistrict.Southern);


        all.addAll(central);
        all.addAll(southern);
        all.addAll(eastern);
        all.addAll(northeastern);
        all.addAll(northern);
        all.addAll(northwestern);
        all.addAll(western);
        all.addAll(southwestern);
        all.addAll(southern);



    }

    private static void initMap(Collection<String> collection, PoliceDistrict district){
        for (String n : collection) {
            map.put(n, district);
        }
    }

    public static PoliceDistrict ofNeighborhood(String neighborhood) {
        try {
            PoliceDistrict district = map.getOrDefault(neighborhood.trim(), PoliceDistrict.unknown);

            if (district == PoliceDistrict.unknown) {
                return nearMatch(neighborhood);
            }
            else{
                return district;
            }

        } catch (NullPointerException e) {
            return PoliceDistrict.unknown;
        }

    }

    private static PoliceDistrict nearMatch(String neighborhood) {
        ExtractedResult result = FuzzySearch.extractOne(neighborhood, all);
        if (isMatchScoreHighEnough(result)) {
            String neighborhoodNearMatch = result.getString();
            return ofNeighborhood(neighborhoodNearMatch);
        }
        return PoliceDistrict.unknown;
    }

    private static boolean isMatchScoreHighEnough(ExtractedResult result){
        return result.getScore() > FUZZY_SEARCH_MINIMUM_CONFIDENCE_SCORE;
    }

}
