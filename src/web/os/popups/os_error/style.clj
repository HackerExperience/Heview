(ns web.os.popups.os-error.style)

(def xp-ui-color-background
  "#fff")

(defn header []
  [:.os-err-header
   {:flex "0 0"
    :min-height "30px"
    :background "transparent url(\"data:image/gif;base64,R0lGODlhBQAeAMQAAABg/ANl8QBZ6ABG4AFs/j2V/wBa9QFDzwBc6QBR5QBY7gBY5iuQ/wBY8wBV6gBl/QBg+QJq/gBT4QBV5QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAFAB4AAAU+oCCKRVkyKEqsa+C6SBwvNC3deK5PfO8nQKBvOHEYj0iFUtloNg1QKGQ6BVitj2w2wu16v9osdToolw9odAgAOw==\")"
    :margin-left "-1px"
    :border-top "2px solid #0055ea"
    :border-radius "13px 13px 0 0"}
   [:.os-err-title
    {:color "#fff"
     :padding "3px 0px 0px 8px"
     :font-size "18px"
     :text-shadow "0 0 #000"}]
   [:.os-err-close
    {:height "21px"
     :width "21px"
     :position :absolute
     :top "6px"
     :right "10px"
     :background "url(\"data:image/gif;base64,R0lGODlhFQAVAPcAANY2Bel8Yuh2XbIxDudyWedwVNZCFuNdPKoxEq06Ha8lAeyReuFUMd9SL+uLc+uHbtJMKs9GJOdxVuduVOZuUeZsTqozFeRhQ+hiNuRePeRfQeBXNOJNJeVNH9NNK7IuCtBHI60yE7ErBuZqTeRjRONcOudvUuqDauuKceVlRuRiQu6dieVoTemBaOlKEWaH3+NbOuh6YOlQGeNcO+JWM2aT8eBLJeRfPuRgP+h0WWaR5+Q+B+ZrTuRjQ+RKGOZtUaYrDeJLIuRkRudoQve4oeRHE+h5Xq83GeuKcOQ9BeNbOeRPI9tDHOVeN+NMHLQtCNEvAOhUIeBWMeJVMeNYNuhEC+yNdrAnAudtUwVe9/CmlOhyV9YzAudiOOhgMudkO+ZvU+7W0NVFG99TMONZOOl7YawwEeqFbdg/ELAzEuRTJdZJIeBRLOFRLuFPK+NMIONNItBTNOXs/OVQItxFH9ZAE+l+Za81Fuh4XuVeOeZtUOVMGeVoSqw2GOVnSOJQKuhzWfGrmcw+G+yTfO2Vf2aZ9dg3DulIDt5AGOlUH+Xq+uXs++JOJ+VqTAVS4OVrTmaW86w8H/fMwuBVMu+hjQU7zOJUMeqBaNJKKOFWLtEwAerUz+6ahas8H645G+pZJOA4ArVEKP718cw4E89EIel/ZsIqAOhMFu+jkOdyVul9ZOVACeVDDeZEDfrh3OVIE9U9DuZuUuZvUudbLOdVJudvVN08FO/Y0tJFHtZGHOZNHOZOHOFPJeRdPd9FHs1JKOhXJeJVKtY5CQVP8uZlQeVXLOdnQd1MKK4xEOFZN6ojAOVoSeh0W+l7YOlcKupeLeRRJ+VTJtVKJeRQH+RkR/O7reJSLeZoReBNKLEoA99VM+uNdeJOIehwVeJWNPXBtehQG+h3Xeh0WOh1WdFGIedSIeXt/eZkPedlPOdlPeh+Zel/ZeJYNtRMKeNZN+ZdMuh5X89HJrQnAeReO9c7C+VnR+dtUbZLMOdNGOVpS+VoS+ZlQ+VmSOVnSeBQKf///yH5BAAAAAAALAAAAAAVABUAAAj/AB3pWPSvoMGDCP+9qKTDlYNmEkbwu0Dixo0ZM2jQsISNTpxbL/5ZQaWF07YHl0oFgGdkXCoKy3BMinBP0b91gQKtWODgTAt1AcqEyyGrUY9kmCwUFLeCEiEUJ1ThAVTrhz4+JHCU2AChT0ETCwahkPSPABYWQjTAIPOv2pQxHhIUHPEAyQmDLKip9WbQWoO4BVO0OGEnnMELvagYZPSHjQdPgWMEiMFsgsESBjkE4eCvHeR/B3KEE0AAzCOE0Ja8gcNL2pGC7uxtASQh1oh6BotFm9OhA7c1dwpayldAQjcTFRDSKrdLl5NcaQq6IcHDhAkKBtMVFBUFHD4fBgYUsLSRwU8FPQaHoMNgUMapImg+FPTFTkU/g9eMfcHwzKCLVvSIUBAibZSgwjf/7EPMOV144cwn/xBxyCoAXFGQLTYwoEQGN8yTRxPvzAJMIjK4UEUSXChQkCFMHNOANhtMIkUmwagxzR6vsLIDKFAo8485v4wiCCnxxBMBCOTgIoYBdcAiDACamNLJP4WEEQoQZiBgAQIhhIDMAB88IUI28igQySY1ZAFJQmwaJEcNwwQEADs=\")"
     :cursor :pointer}]])

(defn body []
  [:.os-err-body
   {:flex "1 1"
    :background-color "#f8f6e6"
    :display :flex
    :flex-direction :row
    }
   [:.os-err-icon
    {:flex "0 0"
     :min-width "65px"
     :background "transparent url(\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACIAAAAiCAMAAAF6nstmAAAB0VBMVEUFBwMQAQEHAAAhAAAyAAA6AAAhEQ1LAAAYGBAXGBZUAABKBgghGBlaAgAoGRdrAAJ0AAF9AAAxIR+DAgCMAACVAAKVAA5kGBefAAAxMSowMi+lBACtAAGlBAU7NC+3AACvAwu3AAe/AAC/AANCODnGAQC9CAC9CAPQAADPAATHBAlUOS9jMjDYAADeAQDJCxPoAADcCAPLDw3pAA3xAADWDgq1IB//AAjgDRn3BgCvJSivJi//Bh2TOjLvEw/LIyXmGRb5EBleV1H8FhLrISH/Hx7/ICbDQTnzLC3YOzt9Y2KUXFT/Ly7VQ0j5NDj/OS/ZR1H+OjX/PkTlS0zCXlveUlGacG7/SEzZWGKCgXn/SVL/UEj/UVT4WVP/WFboY1yegoH4YlythIf/YmDWe2uZloisi5L/amj6cWnWhHz/cnK5k4+koJH/eXvnhX3Hk5Pwh3z2hH3/goaqrZ2uraT/jYuUvb7Kpqf+lJSzuqmew7//nJm7wrH/pJ6oyMv/pKT3rZr1rqfHxqrFybnwva/Dz9Cr2tfR1sTpzb693uHb18jI3dzc38/o3dDI6ezm59Dk6NfY7ezu59Lw5+Dk8N3u79js8N/R//nm9vb5///+//zFwHZ4AAAAAXRSTlMAQObYZgAAAylJREFUOMuVU+1P00AYP/CFoCKbvWKlV0rR0hWd6yqVonEuk7WlhMEGdAIDgaHgdIokGAKRaaLGxWQJn+Tur/WuFcS3D/4+9K6/+z3Pc8/LARChPJyn3wRRnwJwT4aM6v1xBKZGMiYw3QEZCJLLjm6EFvv7m8wGZGdwghgm3SGZTGNZYCSEPMc9Baew9aaxXQ4N/UcjEx4AqTxeOSBWD0hlEwSr6hAQtT2CA9QNgEQQIhyNMwF5yLWHDvp6T/wVq/X6RunJ8e+r8sZ2fblUqSwWMiHhecXPODCM5Fu8bqdylNHsbNrFhNBAaVPrZ4yu6wNfCSYrqoYkgeWN0AGhgScxXhP5MGjACzwSRYEmA9uPw8UhF49z5zpfgn9iq1R7U1vyfhJObbfxbqdWKYwfEzSFarlUWSh5D6M7L1Z8Z2R4dCTnOFmfMWPFFdy8bRg++XzH1lieXn60hVvJeULmLDNFReNj2XT6kCZBZhJ6SrkCgKJZViJ5RMi4qpuqdB4AVxnSEz7N/HBQva5JnQA8R1oiRxUYN5EiCZeoaxEFmEzLqIW/0WRZxe4JAnYRQgK+y8O28IoihLQUggAh15GL0oARuHhH33GqtyBH0d2WO1UPv+9an/9nmT5urS4tVBaXNt//tYofCqVqfWe/0Wjs1DeWy8Wx9d8Em155tb6zu7u7Xa/XahvV6nJpynl4OkC+sFCprq4ul4r5/CPHKRYLhami52UfnHRqjHFBsxkMGwyj6dze3oSTydp2T6T4Ynt5J/+4SYvcDGg7dH+PbvGcYZsp82oombNtw8lkjMkWPWmtrTEtmUlblqVpihg+l1RKs+5kjWRSn2yREHhG1VXLVBVNUi6zWbqraaqpW3oyef3ZUah4PaBS6NSH2H+eTf+sogwOqWpC9g9w5IUcBjJtqywJEhdNpCAheRBFAlfpnz5idwokJPK8cC7q0guZR9L9TxgTF8mKIva7bIbu8zzkL3TO/6iMxFO4s4KIRDoFSOyZdeO0sfDsiYI+TzoL1AiyqaBrD219nOtq7/3l5QcxntHMNlxjZ38TRLh5sas7Fot1dZxpv5SbB/+B744u2u4QJBlwAAAAAElFTkSuQmCC\") no-repeat 15px 15px"}]
   [:.os-err-message
    {:flex "1 1"
     :align-self :center
     :font-size "12px"
     :margin-top "-20px"}]])

(defn footer []
  [:.os-err-footer
   {:flex "0 0"
    :min-height "40px"
    :display :flex
    :justify-content :center
    :background "#f8f6e6"}
   [:button
    {:background "#f8f6e6"
     :outline "1px solid rgba(0,0,0,0.7)"
     :outline-offset "1px"
     :border "1px dotted #aaa"
     :height "60%"
     :align-self :center
     :width "80px"
     :margin-bottom "10px"
     :box-shadow "1px 1px 0 1px rgba(0,0,0,0.40)"}
    [:&:hover
     {:background "#dfddcd"
      :cursor :pointer}]]])

(defn container []
  [:.os-err-container
   {:display :flex
    :flex-direction :column
    :height "100%"
    :width "100%"
    :border-left (str "3px ridge #0055ea")
    :border-right (str "4px ridge #0055ea")
    :border-bottom (str "3px ridge #0055ea")
    :border-radius "15px 15px 0 0"
    }
   [[(header)]
    [(body)]
    [(footer)]]])

(defn local-style []
  [:.full-app-entrypoint
   [:.app-type-os-os-error
    {:background :transparent}
    [:.full-app-container {}
     [(container)]]]
   [:.app-focused
    {:border :none
     :box-shadow :none}]])
