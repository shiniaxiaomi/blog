package com.lyj.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController {

    @RequestMapping("getData")
    public String getData(){

        return "[\n" +
                "    {\n" +
                "        \"value\": \"Books\",\n" +
                "        \"id\": \"Books\",\n" +
                "        \"opened\": true,\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"value\": \"Thrillers\",\n" +
                "                \"id\": \"Thrillers\",\n" +
                "                \"opened\": true,\n" +
                "                \"items\": [\n" +
                "                    {\n" +
                "                        \"value\": \"Bestsellers\",\n" +
                "                        \"id\": \"Bestsellers\",\n" +
                "                        \"opened\": true,\n" +
                "                        \"icon\": {\n" +
                "                            \"folder\": \"fas fa-book\",\n" +
                "                            \"openFolder\": \"fas fa-book-open\",\n" +
                "                            \"file\": \"fas fa-file\"\n" +
                "                        },\n" +
                "                        \"items\": [\n" +
                "                            {\n" +
                "                                \"value\": \"Lawrence Block\",\n" +
                "                                \"id\": \"Lawrence Block\",\n" +
                "                                \"icon\": {\n" +
                "                                    \"folder\": \"fas fa-book\",\n" +
                "                                    \"openFolder\": \"fas fa-book-open\",\n" +
                "                                    \"file\": \"fas fa-file\"\n" +
                "                                }\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Robert Crais\",\n" +
                "                        \"id\": \"Robert Crais\",\n" +
                "                        \"icon\": {\n" +
                "                            \"folder\": \"fas fa-book\",\n" +
                "                            \"openFolder\": \"fas fa-book-open\",\n" +
                "                            \"file\": \"fas fa-file\"\n" +
                "                        }\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Ian Rankin\",\n" +
                "                        \"id\": \"Ian Rankin\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"James Johns\",\n" +
                "                        \"id\": \"James Johns\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Nancy Atherton\",\n" +
                "                        \"id\": \"Nancy Atherton\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"value\": \"History\",\n" +
                "                \"id\": \"History\",\n" +
                "                \"items\": [\n" +
                "                    {\n" +
                "                        \"value\": \"John Mack Faragher\",\n" +
                "                        \"id\": \"John Mack Faragher\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Jim Dwyer\",\n" +
                "                        \"id\": \"Jim Dwyer\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Larry Schweikart\",\n" +
                "                        \"id\": \"Larry Schweikart\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"R. Lee Ermey\",\n" +
                "                        \"id\": \"R. Lee Ermey\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"value\": \"Horror\",\n" +
                "                \"id\": \"Horror\",\n" +
                "                \"items\": [\n" +
                "                    {\n" +
                "                        \"value\": \"Stephen King\",\n" +
                "                        \"id\": \"Stephen King\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Dan Brown\",\n" +
                "                        \"id\": \"Dan Brown\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Mary Janice Davidson\",\n" +
                "                        \"id\": \"Mary Janice Davidson\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Katie Macalister\",\n" +
                "                        \"id\": \"Katie Macalister\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"value\": \"Fiction & Fantasy\",\n" +
                "                \"id\": \"Fiction & Fantasy\",\n" +
                "                \"items\": [\n" +
                "                    {\n" +
                "                        \"value\": \"Audrey Niffenegger\",\n" +
                "                        \"id\": \"Audrey Niffenegger\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Philip Roth\",\n" +
                "                        \"id\": \"Philip Roth\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"value\": \"Teens\",\n" +
                "                \"id\": \"Teens\",\n" +
                "                \"items\": [\n" +
                "                    {\n" +
                "                        \"value\": \"Joss Whedon\",\n" +
                "                        \"id\": \"Joss Whedon\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Meg Cabot\",\n" +
                "                        \"id\": \"Meg Cabot\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Garth Nix\",\n" +
                "                        \"id\": \"Garth Nix\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"value\": \"Ann Brashares\",\n" +
                "                        \"id\": \"Ann Brashares\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"value\": \"Magazines\",\n" +
                "        \"id\": \"Magazines\",\n" +
                "        \"open\": true,\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"value\": \"Sport\",\n" +
                "                \"id\": \"Sport\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";
    }

}
