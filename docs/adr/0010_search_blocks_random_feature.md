# 9. Active search blocks random workout feature

Date: 2020-03-11

## Status

Accepted

## Context

It was planned to use a "Choose random workout" menu option on an already filtered list (by search). Problem here is, that
the menu button is no longer visible if a search query was entered. So either we need to move the random workout button 
somewhere else, or move the search query input field.

## Decision

As more filter options are planned (apart from the search), we need a dedicated screen real estate to enter search and 
filter options.
We decided to create a BottomSheet for search and filter, which will be opened through an ActionBar item (or menu item).
This is future proof because we sure as hell have enough screen real estate and can simply extend the BottomSheet. 

## Consequences

The currently implemented search view will be removed and we need to implement a BottomSheet which will be opened through
an action- or menu item. ViewModel search logic can remain unchanged.